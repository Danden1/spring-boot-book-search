package com.project.book.User;

import java.util.HashMap;
import java.util.Map;

import com.project.book.Security.JwtTokenProvider;
import lombok.NoArgsConstructor;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.book.Exception.MyException;
import com.project.book.Security.JwtTokenDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
@Api("user controller")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public ResponseEntity getLogin(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiResponse(code = 422, message = "로그인 실패(유저 정보 불일치)")
    public ResponseEntity<JwtTokenDTO> postLogin(@RequestBody UserDTO reqBody){
        String email = reqBody.getEmail();
        String pwd = reqBody.getPwd();
        JwtTokenDTO token;
        
        token = userService.login(email, pwd);
        
        
        return new ResponseEntity<JwtTokenDTO>(token, HttpStatus.OK);
    }

    @GetMapping("/signup")
    public ResponseEntity getSignup(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/signup")
    @ApiResponse(code = 422, message = "비밀번호 불일치 또는 이미 존재하는 이메일")
    public ResponseEntity<UserResponseDTO> postSignup(@RequestBody SignupDTO reqBody){
        String email = reqBody.getEmail();
        String pwd1 = reqBody.getPwd1();
        String pwd2 = reqBody.getPwd2();
        String name = reqBody.getName();
        MyUser user;


        if(!pwd1.equals(pwd2)){
            throw new MyException("same not pwd", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        
        user = userService.signup(email, pwd1, name);

        return new ResponseEntity<>(modelMapper.map(user, UserResponseDTO.class), HttpStatus.OK);
    }

    @PostMapping("/token")
    @ApiResponse(code = 422, message = "올바르지 않은 토큰 형식")
    public ResponseEntity<JwtTokenDTO> refreshToken(@RequestBody JwtTokenDTO reqBody) {
        String refreshToken = reqBody.getRefreshToken();
        String accessToken = reqBody.getAccessToken();

        JwtTokenDTO jwtToken; 

        
        jwtToken = userService.updateToken(accessToken, refreshToken);
        
        
        
        
        return new ResponseEntity<JwtTokenDTO>(modelMapper.map(jwtToken, JwtTokenDTO.class), HttpStatus.OK);
    }

    @GetMapping("/info")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<UserResponseDTO> userInfo(HttpServletRequest request){
        String email = jwtTokenProvider.getAccessTokenEmail((jwtTokenProvider.resolveToken(request)));


        return new ResponseEntity<UserResponseDTO>(modelMapper.map(userService.getUserInfo(email), UserResponseDTO.class),HttpStatus.OK);
    }
    
}
