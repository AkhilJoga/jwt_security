package com.jwt.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.demo.entity.JWTRequest;
import com.jwt.demo.entity.JWTResponse;
import com.jwt.demo.service.UserService;
import com.jwt.demo.utility.JWTUtility;


@RestController
public class HomeController {

	@Autowired 
	private JWTUtility jwtUtility;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String hello() {
		return "Hello JWT";
	}
	
	@PostMapping("/authenticate")
	public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception{
		try {
			
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword())
					);
			
		}catch(BadCredentialsException e) {
			throw new Exception("Invalid_Credentials",e);
		}
	}
}
