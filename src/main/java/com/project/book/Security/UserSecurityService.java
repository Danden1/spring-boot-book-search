package com.project.book.Security;


import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.book.User.UserRepository;

import lombok.RequiredArgsConstructor;

import com.project.book.User.MyUser;


@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByEmail(username);
        List<GrantedAuthority> auth = new ArrayList<>();

        if(user == null){
            throw new UsernameNotFoundException("User " + username + "not found");
        }

        auth.add(user.getUserRole());

        return new User(user.getEmail(), user.getPwd(), auth);
    }

    
}
