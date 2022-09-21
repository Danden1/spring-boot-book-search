package com.project.book.Security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.dsig.spec.XPathType.Filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.book.Exception.MyException;


public class JwtFilter extends OncePerRequestFilter{
    private JwtTokenProvider jwtTokenProvider;
    
    public JwtFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = jwtTokenProvider.resolveToken(request);

        //token이 널인 경우엔는 에러를 발생하지 않도록 하는 filter하나 더?
        try{
            if(token != null && jwtTokenProvider.validateAccessToken(token)){
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch(MyException ex){
            SecurityContextHolder.clearContext();
            response.sendError(ex.getHttpStatus().value(), ex.getMessage());
        }

        filterChain.doFilter(request, response);
        
    }

    
}
