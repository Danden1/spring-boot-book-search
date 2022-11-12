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

    @Value("${security.jwt.token.access-secret-key}")
    private String accessSecretKey;

    @Value("${security.jwt.token.refresh-secret-key}")
    private String refreshSecretKey;

    @Value("${security.jwt.token.access-token-valid-time}")
    private long accessTokenValidTime;

    @Value("${security.jwt.token.refresh-token-valid-time}")
    private long refreshTokenValidTime;


    @Autowired
    private UserSecurityService userSecurityService;


    @PostConstruct void init(){
        accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }


    public String createAccessToken(String email){
        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date validDate = new Date(now.getTime() + accessTokenValidTime);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validDate)
            .signWith(SignatureAlgorithm.HS256, accessSecretKey)
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
            .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
            .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = null;

        this.validateAccessToken(token);


        userDetails = userSecurityService.loadUserByUsername(getAccessTokenEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getAccessTokenEmail(String token){
        if(token == null){
            return null;
        }
        try{
            return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token).getBody().getSubject();
        }
        catch(ExpiredJwtException e){
            
            return e.getClaims().getSubject();
        }
        catch(Exception e){
            throw new MyException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public String getRefreshTokenEmail(String token){
        if(token == null){
            return null;
        }
        try{
            return Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token).getBody().getSubject();
        }
        catch(ExpiredJwtException e){

            return e.getClaims().getSubject();
        }
        catch(Exception e){
            throw new MyException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
            Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException e){
            throw new AccessExpireException();
        } catch (JwtException | IllegalArgumentException e){
            throw new MyException("Invalid Access Token", HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean validateRefreshToken(String token){
        try{
            Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException e){
            throw new RefreshExpireException(token);
        } catch (JwtException | IllegalArgumentException e){
            throw new MyException("Invalid Refresh Token", HttpStatus.UNAUTHORIZED);
        }
    }
}
