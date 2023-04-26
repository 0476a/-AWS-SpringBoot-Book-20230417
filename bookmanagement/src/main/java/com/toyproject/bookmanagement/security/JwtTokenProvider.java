package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;
import com.toyproject.bookmanagement.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	// 토큰을 검증해주는 메소드
	public boolean validateToken(String token) {
		try {
			// 토큰을 검증해주는 역할
			Jwts.parserBuilder()
				.setSigningKey(key) // 키값으로 암호화된 값을 푼다. (서명검증에 필요한 키값을 설정)
				.build()
				.parseClaimsJws(token); // 요청받은 토큰 값과 맞는지 확인해서 검증된 객체를 만들어줌.
			
			// 검증 되었으면 true 반환
			return true;
		} catch (SecurityException | MalformedJwtException e){
//			log.info("Invalid JWT Token", e);
			// security 라이브러리 문제, JSON의 포멧이 잘못된 JWT가 들어옴.
		} catch (ExpiredJwtException e) {
//			log.info("Expired JWT Token", e);
			// 토큰의 유효기간이 만료된 경우
		} catch (UnsupportedJwtException e) {
//			log.info("Unsupported JWT Token", e);
			// jwt의 형식을 지키지 않은 경우
		} catch (IllegalArgumentException e) {
//			log.info("IllegalArgument JWT Token", e);
			// jwt 토큰이 없는 경우
		} catch (Exception e) {
//			log.info("JWT Token Error", e);
			// 나머지 모든 예외
		}
		
		// 예외 발생하면 false 반환
		return false;
	}
	
	
	// Bearer 때주기
	public String getToken(String token) {
		String type = "Bearer";
		if(StringUtils.hasText(token) && token.startsWith(type)) {
			return token.substring(type.length() + 1);
		} 
		
		return null;
	}
	
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public Authentication getAuthentication(String accessToken) {
		Authentication authentication = null;
		
		Claims claims = getClaims(accessToken);
		if(claims.get("auth") == null) {
			throw new CustomException("AccessToken에 권한 정보가 없습니다.");
		}
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		String auth = claims.get("auth").toString();
		for(String role : auth.split(",")) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		
		
		UserDetails userDetails = new User(claims.getSubject(), "", authorities);
		
		authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
		
		return authentication;
	}
}
