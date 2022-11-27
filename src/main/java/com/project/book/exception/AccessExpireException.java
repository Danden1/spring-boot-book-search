package com.project.book.exception;

import org.springframework.http.HttpStatus;

public class AccessExpireException extends MyException{
    
    public AccessExpireException(){
        super("expire access token. need refresh token and access token" ,HttpStatus.UNAUTHORIZED);
        
    }
}
