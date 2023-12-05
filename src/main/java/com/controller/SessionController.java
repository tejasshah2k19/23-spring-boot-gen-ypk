package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bean.UserBean;
import com.dao.UserDao;
import com.dto.LoginDto;
import com.service.MailerService;

@RestController
public class SessionController {

	// we dont have views --- input jsp
	// input ?

	// @GetMapping
	// open -> jsp -> input -> submit -> saveuser {post}

	// fn em password
	// spring -> input read ? using bean

	@Autowired
	UserDao userDao;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	MailerService mailerService;

	@PostMapping("/users")
	public ResponseEntity<?> saveUser(@RequestBody UserBean userBean) {
		System.out.println(userBean.getFirstName());
		System.out.println(userBean.getEmail());
		System.out.println(userBean.getPassword());
		//
		String pwd = userBean.getPassword(); // plain text

		String ePwd = encoder.encode(pwd);
		userBean.setPassword(ePwd);

		userDao.addUser(userBean);

		Runnable r = () -> {
			mailerService.sendWelcomeMail(userBean.getEmail(), userBean.getFirstName());
		};
		Thread t  = new Thread(r);
		t.start();
		

//		return ResponseEntity.ok(userBean);//201
//		return ResponseEntity.status(201).body(userBean);
		return ResponseEntity.status(HttpStatus.CREATED).body(userBean);

	}

	@GetMapping("/users")
	public List<UserBean> getAllUsers() {
		//
		return userDao.getAllUsers();
	}

	@DeleteMapping("/users/{userId}")
	public UserBean deleteUser(@PathVariable("userId") Integer userId) {
		UserBean user = userDao.getUserById(userId);
		if (user == null) {
			return null;
		} else {

			return user;
		}

	}

	@GetMapping("/users/{userId}")
	public UserBean getUserById(@PathVariable("userId") Integer userId) {
		UserBean user = userDao.getUserById(userId);
		if (user == null) {
			return null;
		} else {

			return user;
		}
	}

	@PutMapping("/users")
	public UserBean updateUser(@RequestBody UserBean user) {
		userDao.updateUser(user);
		return user;
	}

	// db encoded
	// user plainText

	@PostMapping("/login")
	public UserBean login(@RequestBody LoginDto loginDto) {

		UserBean user = userDao.getUserByEmail(loginDto.getEmail());
		if (user != null && encoder.matches(loginDto.getPassword(), user.getPassword()) == true) {
			return user;
			// 200
		}

		return null;
		// 401
	}

}
