package com.gm.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    String secret = "easyapi123456789easyapi123456789easyapi123456789easyapi123456789easyapi123456789";

    private Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    private long tokenValidityInMilliseconds = 1000 * 24L * 60L * 60L * 30L * 120;

    private long tokenValidityInMillisecondsForRememberMe = 1000 * 24L * 60L * 60L * 30L * 120;


    // Generate token
    public String createToken(Authentication authentication, Boolean rememberMe) {
        // Get all permissions and assemble them into a string
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        // Set the expiration time of the token, distinguish whether to remember me, remember that my expiration time is long
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        // Generate token string info
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // Obtain authentication information through tokens
    public Authentication getAuthentication(String token) {
        // Reversely generate the claim object through the token
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Obtain auth object information with claim
        Object obj = claims.get(AUTHORITIES_KEY);
        if(obj != null && StringUtils.isNotEmpty(obj.toString())) {
            // Split string field to generate auth collection information
            authorities = Arrays.stream(obj.toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        // Generate user object
        User principal = new User(claims.getSubject(), "", authorities);

        // Return token
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // Verify that the token is still valid
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}