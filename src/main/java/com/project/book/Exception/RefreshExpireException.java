package com.project.book.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;


@Getter
public class RefreshExpireException extends MyException{
    private final String refreshToken;

    public RefreshExpireException(String str){
        super("expire refresh token. login again." ,HttpStatus.UNPROCESSABLE_ENTITY);
        refreshToken = str;
        
    }

}
