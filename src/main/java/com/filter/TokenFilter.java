package com.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bean.UserBean;
import com.dao.UserDao;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenFilter implements Filter {

	@Autowired
	UserDao userDao;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// green signal -> go ahead -> chain
		System.out.println("Filter Called........");
		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("token");
		System.out.println("token => " + token);
		String url1 = req.getRequestURL().toString();
		String url2 = req.getRequestURI();

		System.out.println("url => " + url1);
		System.out.println("urI => " + url2);

		if (url1.contains("public")) {
			chain.doFilter(request, response);// goahead
		} else {
			// private
			// admin | user
			// token ?
			if (token == null || token.trim().length() == 0) {
				System.out.println("TOKEN is MISSING");
				return;
			} else {
				UserBean user = userDao.getUserByToken(token); // user NULL
				if (user == null) {
					System.out.println("Invalid TOKEN");
					return;
				} else if (user.getRole() == null) {

					System.out.println("Invalid USER ");
					return;
				} else {// admin
						// url ROLE
						// admin ADMIN
						// admin USER
						// user ADMIN
						// user USER

					// true && true
					if (url1.contains("admin") && !user.getRole().equals("ADMIN")) {
						System.out.println("UNAUTHORIZED");
						return;
					}
					chain.doFilter(request, response);
				}
			}
		}

	}

}
