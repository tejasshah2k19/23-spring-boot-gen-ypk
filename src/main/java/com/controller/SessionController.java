package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.ResponseBean;
import com.bean.UserBean;
import com.dao.UserDao;
import com.dto.LoginDto;
import com.service.MailerService;

@RestController
@RequestMapping("/public")
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

	@PostMapping("/signup") //signup 
	public ResponseBean<?> saveUser(@RequestBody UserBean userBean) {
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
		ResponseBean<UserBean> res = new ResponseBean<>();
		res.setData(userBean);
		res.setStatus(200);
		res.setMsg("User Added ");
//		return ResponseEntity.status(HttpStatus.CREATED).body(userBean);
		return res; 
	}


	//status 
	//data 
	

	// db encoded
	// user plainText

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

		UserBean user = userDao.getUserByEmail(loginDto.getEmail());
		if (user != null && encoder.matches(loginDto.getPassword(), user.getPassword()) == true) {
		
			//token generate - 56437
		String token = 	"" + (int)(Math.random()*100000); //    * 100000
		
		//user -> set 
			user.setToken(token);
			//user -> db - set 
			userDao.updateToken(user.getUserId(),token);//
			
			return ResponseEntity.ok(user);
			// 200
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginDto);
		// 401
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("token") String token){
		
		UserBean user = userDao.getUserByToken(token);
		
		userDao.updateToken(user.getUserId(), "");
		
		return ResponseEntity.ok(null);
	}

}
