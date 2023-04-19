package com.toyproject.bookmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	
	// 애러 던져주는 것은 service에서 할 것! (이거 기억안남!)
	public void checkDuplicatedEmail(String email) {
		
		if(userRepository.findUserByEmail(email) != null) {
			
			throw new CustomException("Duplicated Email",
					ErrorMap.bulider().put("email", "사용중인 이메일입니다.").build());
		}
	}
	
	public void signup(SignupReqDto signupReqDto) {
		User userEntity = signupReqDto.toEntity();
		userRepository.saveUser(userEntity);
		
		List<Authority> authorities = new ArrayList<>();
		authorities.add(Authority.builder().userId(userEntity.getUserId()).roleId(1).build());
		
		userRepository.addAuthorities(authorities);
	}
}
