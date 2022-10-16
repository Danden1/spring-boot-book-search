package com.project.book.Security;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.dsig.spec.XPathType.Filter;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.book.Exception.MyException;
import com.project.book.Exception.RefreshExpireException;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter{
    final private JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = jwtTokenProvider.resolveToken(request);

        //token이 널인 경우엔는 에러를 발생하지 않도록 하는 filter하나 더?
        try{
            
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            
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
        }

        filterChain.doFilter(request, response);
        
    }

    
}
