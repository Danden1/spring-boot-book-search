package com.project.book.User;

import com.project.book.Exception.MyException;
import com.project.book.Exception.RefreshExpireException;
import com.project.book.Security.JwtTokenDTO;
import com.project.book.Security.JwtTokenProvider;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenDTO login(String email, String pwd){
        MyUser user = userRepository.findByEmail(email);
        
        if(user != null && passwordEncoder.matches(pwd, user.getPwd())){
            String accessToken = jwtTokenProvider.createAccessToken(email); 
            String refreshToken = jwtTokenProvider.createRefreshToken(email);

            JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();

            jwtTokenDTO.setAccessToken(accessToken);
            jwtTokenDTO.setRefreshToken(refreshToken);


            return jwtTokenDTO;
            
        }
        else{
            throw new MyException("invalid password/email", HttpStatus.valueOf(422));
        }
    }

    //this project use not admin role.
    public MyUser signup(String email, String pwd, String name){
        MyUser user = userRepository.findByEmail(email);
        UserRole userRole = UserRole.valueOf("ROLE_CLIENT");
        if(user != null){
            throw new MyException("already exist email", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        user = new MyUser();
        pwd = passwordEncoder.encode(pwd);
        try{
            user.setEmail(email);
        }
        catch(Exception ex){
            throw new MyException(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        user.setName(name);
        user.setPwd(pwd);
        user.setUserRole(userRole);
        
        userRepository.save(user);

        return user;
    }


    public JwtTokenDTO updateToken(String accessToken, String refreshToken){
        jwtTokenProvider.validateRefreshToken(refreshToken);

        String email = jwtTokenProvider.getEmail(refreshToken);

        if(!email.equals(jwtTokenProvider.getEmail(accessToken)) || email == null){
            throw new MyException("Invalid access/refresh token.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        

        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();

        accessToken = jwtTokenProvider.createAccessToken(email);
        refreshToken = jwtTokenProvider.createRefreshToken(email);
        
        jwtTokenDTO.setAccessToken(accessToken);
        jwtTokenDTO.setRefreshToken(refreshToken);

        

        return jwtTokenDTO;

    }

    

}
