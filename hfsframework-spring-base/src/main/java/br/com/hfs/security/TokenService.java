package br.com.hfs.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	
	@Value("${hefesto.jwt.expiration}")
	private String expiration;
	
	@Value("${hefesto.jwt.secret}")
	private String secret;

	private Key getChave() {
		byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
		Key chave = Keys.hmacShaKeyFor(keyBytes);
		return chave;
	}

	public String gerarToken(Authentication authentication) {
		HfsUserDetails user = (HfsUserDetails) authentication.getPrincipal();
		Date today = new Date();
		Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("hfsframework-spring-api")
				.setSubject(user.getId().toString())
				.setIssuedAt(today)
				.setExpiration(expirationDate)
				.signWith(getChave(), SignatureAlgorithm.HS256)
				//.setPayload(user.getPayload())
				.setClaims(user.getClaims())
				.compact();
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getChave())
				.build().parseClaimsJws(token);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUser(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getChave())
				.build().parseClaimsJws(token)
				.getBody();
		
		//return Long.parseLong(claims.getSubject());
		return Long.parseLong(claims.get("id").toString());
	}

}
