package com.project.book.Exception;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.book.Security.JwtTokenRepository;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    
    private final JwtTokenRepository jwtTokenRepository;
    // @ExceptionHandler(AccessExpireException.class)
    // public void handleAccessExpireException(HttpServletResponse res, AccessExpireException ex) throws IOException{
    //     res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    // }

    // @ExceptionHandler(RefreshExpireException.class)
    // public void handleAccessExpireException(HttpServletResponse res, RefreshExpireException ex) throws IOException{
    //     res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    // }

    @ExceptionHandler(MyException.class)
    public ResponseEntity handleMyException(HttpServletResponse res, MyException ex) throws IOException{
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(RefreshExpireException.class)
    public ResponseEntity handleRefreshExpireException(HttpServletResponse res, RefreshExpireException ex) throws URISyntaxException,IOException{
        jwtTokenRepository.delete(jwtTokenRepository.findByrefreshToken(ex.getRefreshToken()));
        URI redirectURI = new URI("http://localhost:8080/users/login");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectURI);

        return new ResponseEntity(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
