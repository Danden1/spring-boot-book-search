package com.project.book.User;

import com.project.book.Exception.MyException;
import com.project.book.Security.JwtToken;
import com.project.book.Security.JwtTokenProvider;
import com.project.book.Security.JwtTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

    public JwtToken login(String email, String pwd){
        MyUser user = userRepository.findByEmail(email);

        if(user != null && user.getPwd() == pwd){
            String accessToken = jwtTokenProvider.createAccessToken(email); 
            String refreshToken = jwtTokenProvider.createRefreshToken(email);

            JwtToken jwtToken = new JwtToken();

            jwtToken.setAccessToken(accessToken);
            jwtToken.setRefreshToken(refreshToken);

            jwtTokenRepository.save(jwtToken);

            return jwtToken;
            
        }
        else{
            throw new MyException("invalid password/email", HttpStatus.valueOf(422));
        }
    }

    public void signup(String email, String pwd, String name){
        MyUser user = userRepository.findByEmail(email);

        if(user != null){
            throw new MyException("already exist email", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        try{
            user.setEmail(email);
        }
        catch(Exception ex){
            throw new MyException(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        user.setName(name);
        user.setPwd(pwd);
    
        userRepository.save(user);
    }

    

}
