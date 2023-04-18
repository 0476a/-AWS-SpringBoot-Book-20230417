package com.toyproject.bookmanagement.exception;

import java.util.Map;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

	private Map<String, String> errorMap;
	
	// 애러 메시지만 받는 경우
	public CustomException(String message) {
		// RuntimeException에 message를 등록을 시켜준다.
		super(message);
	}
	
	// 애러 메시지랑 멥을 같이 받는 경우
	public CustomException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = errorMap;
	}
}
