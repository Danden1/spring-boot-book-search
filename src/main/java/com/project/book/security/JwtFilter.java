package com.project.book.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.book.exception.MyException;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter{
    final private JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = jwtTokenProvider.resolveToken(request);

        try{
            if(token != null){
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            
        }
        catch(MyException ex){
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = new HashMap<>();
                     
            map.put("error_message", ex.getMessage());
            response.reset();
            response.setStatus(ex.getHttpStatus().value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            //occur error
            // PrintWriter out = response.getWriter();
            ServletOutputStream out = response.getOutputStream();
            
            out.print(mapper.writeValueAsString(map));
            out.flush();
            out.close();
            
            return;
        }

        filterChain.doFilter(request, response);
        
    }

    
}
