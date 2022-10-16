package com.project.book.Security;

import org.springframework.stereotype.Component;

import com.project.book.Exception.AccessExpireException;
import com.project.book.Exception.MyException;
import com.project.book.Exception.RefreshExpireException;

import java.util.Date;
import java.util.Base64;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
        UserDetails userDetails = null;

        if(this.validateAccessToken(token)){
            userDetails = userSecurityService.loadUserByUsername(getEmail(token));
        }
        else{
            return null;
        }
        try{
            if(this.validateAccessToken(token)){
                userDetails = userSecurityService.loadUserByUsername(getEmail(token));
            }
        }
        catch(Exception e){
            System.out.println("auth func");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token){
        if(token == null){
            return null;
        }
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req){
        String token = req.getHeader("X-Auth-Token");

        if(token != null){
            return token;
        }

        return null;
    }
    
    public boolean validateAccessToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException e){
            System.out.println("token func1");
            throw new AccessExpireException();
        } catch (JwtException | IllegalArgumentException e){
            System.out.println("token func2");
            throw new MyException("Invalid Token", HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean validateRefreshToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException e){
            throw new RefreshExpireException(token);
        } catch (JwtException | IllegalArgumentException e){
            throw new MyException("Invalid Token", HttpStatus.UNAUTHORIZED);
        }
    }
}
