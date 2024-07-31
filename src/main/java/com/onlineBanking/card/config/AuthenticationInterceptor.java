package com.onlineBanking.card.config;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

	@Value("${jwt.secret}")
	private String secretKey;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

		// Extract token from the header
		System.out.println(authHeader);
		String token = authHeader.substring(7); // Remove "Bearer " prefix
		System.out.println(token);
		// Perform your token validation here
		boolean isValidToken = validateToken(token);
		if (!isValidToken) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

		Long userId = getUserIdFromToken(token);
		if (userId == null) {
			return false;
		}

		request.setAttribute("userId", userId);
		return true;
	}

	private boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		return claims.get("userId", Long.class);
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
