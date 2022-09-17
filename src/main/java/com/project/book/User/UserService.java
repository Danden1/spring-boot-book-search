package com.project.book.User;

import com.project.book.Security.JwtTokenProvider;

import org.springframework.stereotype.Service;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    

}
