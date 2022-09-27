package com.project.book.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;


@Getter
public class RefreshExpireException extends MyException{
    private String refreshToken;

    public RefreshExpireException(String str){
        super("expire refresh token. login again." ,HttpStatus.UNAUTHORIZED);
        refreshToken = str;
        
    }

}
