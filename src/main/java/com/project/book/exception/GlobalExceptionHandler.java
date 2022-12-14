package com.project.book.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    


    @ExceptionHandler(MyException.class)
    public ResponseEntity<Map<String, Object>> handleMyException(HttpServletResponse res, MyException ex) throws IOException{
        Map<String,Object> map = new HashMap<>();
        map.put("error_message", ex.getMessage());

        return ResponseEntity.status(ex.getHttpStatus()).body(map);
    }


//    client에서 redirect 처리
//    @ExceptionHandler(RefreshExpireException.class)
//    public ResponseEntity handleRefreshExpireException(HttpServletResponse res, RefreshExpireException ex) throws URISyntaxException,IOException{
//        URI redirectURI = new URI("http://localhost:8080/users/login");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(redirectURI);
//
//        return new ResponseEntity(httpHeaders, HttpStatus.SEE_OTHER);
//    }

}
