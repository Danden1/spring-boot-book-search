package com.project.book.User;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.book.Exception.MyException;
import com.project.book.Security.JwtToken;
import com.project.book.Security.JwtTokenDTO;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/login")
    public ResponseEntity getLogin(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> postLogin(@RequestBody Map<String, Object> reqBody){
        String email = reqBody.get("email").toString();
        String pwd = reqBody.get("pwd").toString();
        JwtToken token;
        
        token = userService.login(email, pwd);
        
        
        return new ResponseEntity<JwtTokenDTO>(modelMapper.map(token, JwtTokenDTO.class), HttpStatus.OK);
    }

    @GetMapping("/signup")
    public ResponseEntity getSignup(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Integer> postSignup(@RequestBody Map<String, Object> reqBody){
        String email = reqBody.get("email").toString();
        String pwd1 = reqBody.get("pwd1").toString();
        String pwd2 = reqBody.get("pwd2").toString();
        String name = reqBody.get("name").toString();

        if(!pwd1.equals(pwd2)){
            throw new MyException("not same pwd", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<Integer>(userService.signup(email, pwd1, name), HttpStatus.OK);
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String,Object> reqBody) {
        String refreshToken = reqBody.get("refresh_token").toString();
        String accessToken = reqBody.get("access_token").toString();

        if(userService.validRefreshToken(accessToken, refreshToken)){
            accessToken = userService.updateAccessToken(refreshToken);
        }
        else{
            throw new MyException("invalid token.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("access_token", accessToken);
        
        return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<String>("hi", HttpStatus.OK);
    }
    
}
