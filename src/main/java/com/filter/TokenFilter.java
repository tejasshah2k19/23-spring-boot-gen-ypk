package com.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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

		if (!url1.contains("public")) {// private

			if (token != null || token.trim().length() != 0) {
				// token->time , todayTime
				if (userDao.getUserByToken(token) == null) {
					// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
					
					System.out.println("UNAUTHORIZED");
				} else {
					chain.doFilter(request, response);
				}
			}
		} else {// public

			chain.doFilter(request, response);
		}

	}

}
