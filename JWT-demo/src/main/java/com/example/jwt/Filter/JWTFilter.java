package com.example.jwt.Filter;

import com.example.jwt.Service.UserService;
import com.example.jwt.Utility.JWTUtility;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.impl.DefaultJwt;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtility jwtUtility;
     @Autowired
     private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //passing in the authorizaton
        DefaultJwt<Object> httpServletRequest;
        String authorization = httpServletRequest.getHeader("Authorization");

    }
}
