package com.toyproject.bookmanagement.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyproject.bookmanagement.aop.annotation.ValidAspect;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	
	// WebMvcConfig를 생성해주어서 CrossOrigin어노테이션을 추가해주지 않아도 받을 수 있다.
	@PostMapping("/login")
	@ValidAspect
	public ResponseEntity<?> login(@Valid @RequestBody LoginReqDto loginReqDto, BindingResult bindingResult) {
		
		return ResponseEntity.ok(authenticationService.signin(loginReqDto));
	}
	
	
	// CORS의 문을 열어주는 어노테이션 해당 요청만 열어줌.
	// @CrossOrigin 서버에서만 정해줄 수 있다.
//	@CrossOrigin
	@ValidAspect
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) {
		authenticationService.checkDuplicatedEmail(signupReqDto.getEmail());
		authenticationService.signup(signupReqDto);
		return ResponseEntity.ok().body(true);
	}
	
	@GetMapping("/authenticated")
	public ResponseEntity<?> authenticated(String accessToken) {
		
		return ResponseEntity.ok().body(authenticationService.authenticated(accessToken));
	}
}
