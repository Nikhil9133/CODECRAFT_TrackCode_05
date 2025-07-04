package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSecretKey(), SignatureAlgorithm.HS512).compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			// Optional: log e.getMessage()
			return false;
		}
	}

	public String getUsername(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(getSecretKey())
	            .build()
	            .parseClaimsJws(token)
	            .getBody()
	            .getSubject();
	}

}
