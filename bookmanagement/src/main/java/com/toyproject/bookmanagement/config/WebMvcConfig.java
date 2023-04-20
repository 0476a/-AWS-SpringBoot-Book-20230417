package com.toyproject.bookmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// CrossOrigin 어노테이션을 한번에 적용시켜주기 위한 것
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("*")
				// 모든 포트에서 받아주는데 테스트일 때만 이렇게 하고 회사가서는 이렇게 하면 안됨!
				.allowedOrigins("*");
				// 3000번 포트에서 오는 값만 다 받아준다.
//				.allowedOrigins("http://localhost:3000");
	}
}
