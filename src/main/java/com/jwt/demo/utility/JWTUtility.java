package com.jwt.demo.utility;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtility implements Serializable {

	private static final long serialVersionUID = 23423445342L;
	
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	//retrieve the user name from JWT token

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token,Claims::getSubject);
	}
	
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token,Claims::getExpiration);
	}
	

	public <T> T getClaimFromToken(String token,Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims getAllClaimsFromToken(String token) {
	
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();	
	}
	
	public String getnerateToken(UserDetails userDetails) {
		Map<String,Object> claims = new HashMap();
		return doGenerateToken(claims,userDetails.getUsername());
	}


	private String doGenerateToken(Map<String, Object> claims, String subject) {
		// TODO Auto-generated method stub
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY *1000))
				.signWith(io.jsonwebtoken.SignatureAlgorithm.HS256,secretKey).compact();
	}
	
	public Boolean validateToken(String token , UserDetails userdetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userdetails.getUsername()) && !isTokenExpired(token));
	}


	private Boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	
}
