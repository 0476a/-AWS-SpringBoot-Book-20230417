package com.toyproject.bookmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.toyproject.bookmanagement.security.JwtAuthenticationEntryPoint;
import com.toyproject.bookmanagement.security.JwtAuthenticationFilter;
import com.toyproject.bookmanagement.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 특정출처에서 오는 요청을 허용할 수 있음.
		http.cors();
		// csrf를 막아줘야함! -> jwt 토큰을 사용할 것이기 때문에
		http.csrf().disable();
		// httpBasic도 사용을 안해줘야함!
		http.httpBasic().disable();
		// 폼로그인도 사용을 안해줘야함!
		http.formLogin().disable();
		// 세션을 사용하지 않겠다고 다음과 같이 설정해준다.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// 요청에 인증을 받아라는 것을 나타냄.
		     // HTTP 요청에 대한 인가 규칙을 정의
		http.authorizeRequests()
			// 경로를 설정해줌.
			.antMatchers("/auth/**")
			// 위 경로에 대한 접근 권한을 모든 사용자에게 허용
			.permitAll()
			// 위 경로에 대한 다른 모든 요청에 대한 설정
			.anyRequest()
			// 모든 요청에 대해서 인증된 사용자만 접근할 수 있게 해줌.
			.authenticated()
			.and()
			//   UsernamePasswordAuthenticationFilter 이전에 JwtAuthenticationFilter 생성되어 실행될 수 있도록 해줌.
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			// 애러가 발생하면 응답을 해줌.
			.authenticationEntryPoint(jwtAuthenticationEntryPoint);
		
		
	}
	
}
