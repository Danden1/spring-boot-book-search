package com.project.book.Security;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Base64;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Setter
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.access-token-valid-time}")
    private long accessTokenValidTime;

    @Value("${security.jwt.token.refresh-token-valid-time}")
    private long refreshTokenValidTime;


    @Autowired
    private UserSecurityService userSecurityService;
    

    @PostConstruct void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public String createAccessToken(String email){
        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date validDate = new Date(now.getTime() + accessTokenValidTime);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public String createRefreshToken(String email){
        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date validDate = new Date(now.getTime() + refreshTokenValidTime);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userSecurityService.loadUserByUsername(getEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    
    
}
