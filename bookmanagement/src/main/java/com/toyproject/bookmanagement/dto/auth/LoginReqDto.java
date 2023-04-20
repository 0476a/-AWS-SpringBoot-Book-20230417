package com.toyproject.bookmanagement.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class LoginReqDto {
	
	@Email
	@NotBlank(message = "이메일을 입력하세요")
	private String email;
	
	// 정규식으로 비밀번호 설정하기 (최소 8글자 최대 16글자, 글자 1개, 숫자 1개, 특수문자 1개) 조건에 맞아야함!
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
			message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8 ~ 16자로 작성하세요.")
	private String password;
}
