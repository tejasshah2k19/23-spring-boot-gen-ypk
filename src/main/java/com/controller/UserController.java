package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.UserBean;
import com.dao.UserDao;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserDao userDao;

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
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("token") String token){
		
		UserBean user = userDao.getUserByToken(token);
		
		userDao.updateToken(user.getUserId(), "");
		
		return ResponseEntity.ok(null);
	}

}
