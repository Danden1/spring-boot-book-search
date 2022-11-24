package com.project.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.book.Security.JwtFilter;
import com.project.book.Security.JwtTokenDTO;
import com.project.book.Security.JwtTokenProvider;
import com.project.book.User.MyUser;
import com.project.book.User.UserRepository;
import com.project.book.User.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.modelmapper.ModelMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    private String email = "admin@gmail.com";
    private String pwd = "12345";
    private String name = "권용하";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;


    @Autowired
    public MockMvc mock;


    private ObjectMapper objectMapper = new ObjectMapper();



    @Nested
    class SignupTest{



        @Test
        public void signupWrongPwdTest() throws Exception{

            try {
                userService.removeUser(email);
            }
            catch(Exception e){
            }
            Map<String, String> map = new HashMap<>();

            map.put("email", email);
            map.put("pwd1", pwd);
            map.put("pwd2", "123");
            map.put("name", name);

            mock.perform(post("/users/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(map))
                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.error_message", is("same not pwd")))
                    .andDo(print());
        }

        @Test
        public void signupTest() throws Exception{
            try {
                userService.removeUser(email);
            }
            catch(Exception e){
            }
            Map<String, String> map = new HashMap<>();

            map.put("email", email);
            map.put("pwd1", pwd);
            map.put("pwd2", pwd);
            map.put("name", name);

            mock.perform(post("/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(map))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.email", is(email)))
                    .andExpect(jsonPath("$.name", is(name)))
                    .andDo(print());
        }

        @Test
        public void signupAlreadyExistTest() throws Exception{
            try {
                userService.signup(email, pwd, email);
            }
            catch(Exception e){
            }
            Map<String, String> map = new HashMap<>();

            map.put("email", email);
            map.put("pwd1", pwd);
            map.put("pwd2", pwd);
            map.put("name", name);

            mock.perform(post("/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(map))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.error_message", is("already exist email")))
                    .andDo(print());
        }
    }


    @Nested
    class LoginTest{

        @Test
        public void wrongPwdTest() throws Exception{
            try {
                userService.signup(email, pwd, email);
            }
            catch(Exception e){
            }


            Map<String, String> map = new HashMap<>();

            map.put("email", email);
            map.put("pwd", "123");

            mock.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(map))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.error_message", is("invalid password/email")))
                    .andDo(print());
        }

        @Test
        public void wrongEmailTest() throws Exception{
            try {
                userService.removeUser(email);
            }
            catch(Exception e){
            }
            Map<String, String> map = new HashMap<>();

            map.put("email", email);
            map.put("pwd", pwd);

            mock.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(map))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.error_message", is("invalid password/email")))
                    .andDo(print());
        }


        @Test
        public void loginTest() throws Exception{
            try {
                userService.signup(email, pwd, email);
            }
            catch(Exception e){
            }
            Map<String, String> map = new HashMap<>();

            map.put("email", email);
            map.put("pwd", pwd);

            mock.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(map))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").isString())
                    .andExpect(jsonPath("$.refreshToken").isString())
                    .andDo(print());
        }
    }


    @Nested
    class TokenTest{
        public void getTokenTest(){

        }
    }

    @Nested
    class UserInfo{

        @Test
        public void notExsitUserTest() throws Exception{
            try {
                userService.removeUser(email);
            }
            catch(Exception e){
            }

            String accessToken = jwtTokenProvider.createAccessToken(email);




            mock.perform(get("/users/info")
                            .header("X-Auth-Token", accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.error_message", is("not exist user")))
                    .andDo(print());
        }

        @Test
        public void notLoginTest() throws Exception{
            mock.perform(get("/users/info")
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }

        @Test
        public void loginTest() throws Exception{
            try {
                userService.signup(email, pwd, name);
            }
            catch(Exception e){
            }

            String accessToken = jwtTokenProvider.createAccessToken(email);




            mock.perform(get("/users/info")
                            .header("X-Auth-Token", accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is(email)))
                    .andExpect(jsonPath("$.name", is(name)))
                    .andDo(print());
        }
    }

}
