package com.project.book.security;


import java.util.ArrayList;
import java.util.List;

import com.project.book.exception.MyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.project.book.user.UserRepository;

import lombok.RequiredArgsConstructor;

import com.project.book.user.MyUser;


@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws MyException{
        MyUser user = userRepository.findByEmail(username);
        List<GrantedAuthority> auth = new ArrayList<>();

        if(user == null){
            throw new MyException("not exist user", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        auth.add(user.getUserRole());

        return new User(user.getEmail(), user.getPwd(), auth);
    }

    
}
