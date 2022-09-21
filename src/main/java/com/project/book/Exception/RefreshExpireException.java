package com.project.book.Exception;

import org.springframework.http.HttpStatus;

public class RefreshExpireException extends MyException{
    
    public RefreshExpireException(){
        super("expire refresh token. login again." ,HttpStatus.UNAUTHORIZED);
        
    }
}
