package com.project.book.Exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // @ExceptionHandler(AccessExpireException.class)
    // public void handleAccessExpireException(HttpServletResponse res, AccessExpireException ex) throws IOException{
    //     res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    // }

    // @ExceptionHandler(RefreshExpireException.class)
    // public void handleAccessExpireException(HttpServletResponse res, RefreshExpireException ex) throws IOException{
    //     res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    // }

    @ExceptionHandler(MyException.class)
    public ResponseEntity handleMyExpireException(HttpServletResponse res, MyException ex) throws IOException{
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }

}
