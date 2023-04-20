package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final Key key;
	
	                     // yml에서 작성한 jwt: secret을 들고 와야함!
	public JwtTokenProvider(@Value("${jwt.secret}")String secretKey) {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	
	public JwtRespDto generateToken(Authentication authentication) {
		
		
		StringBuilder builder = new StringBuilder();
		
		authentication.getAuthorities().forEach(grantedAuthority -> {
			builder.append(grantedAuthority.getAuthority() + ",");
		});
		
		builder.delete(builder.length() - 1, builder.length());
		
		String authorities = builder.toString();
		
		// 이걸 사용한 방법도 있다! 근데 검증된 건지는..??
//		String a = authentication.getAuthorities().toString();
//		a = a.substring(1, a.length() - 1);
		
		Date tokenExpriesDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24)); // 현재시간 + 하
		
		PrincipalUser userDetails = (PrincipalUser)authentication.getPrincipal();
		
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName()) 		// 토큰의 제목
				.claim("auth", authorities) 				// auth
				.setExpiration(tokenExpriesDate) 			// 토큰 만료 시간
				.signWith(key, SignatureAlgorithm.HS256) 	// 토큰 암호화
				.compact();
		
		return JwtRespDto.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.build();
	}
}
