package com.toyproject.bookmanagement.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.toyproject.bookmanagement.dto.common.ErrorResponseDto;
import com.toyproject.bookmanagement.exception.CustomException;

// 전역적인 예외 처리를 수행할 수 있는 클래스이다.
// @ExceptionHandler, @ModelAttribute, @InitBinder가 적용된 메소드들을 AOP 적용하기 위해 만들어진 어노테이션
@RestControllerAdvice
public class AdviceController {

	// 해당 어노테이션을 사용해서 CustomException 예외가 주어지면 해당 메소드에서 처리를 해준다.
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> custiomException(CustomException e) {
		return ResponseEntity.badRequest().body(new ErrorResponseDto<>(e.getMessage(), e.getErrorMap()));
	}
}
