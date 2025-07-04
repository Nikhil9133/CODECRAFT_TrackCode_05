package com.example.demo.security;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired 
    private JwtTokenProvider tokenProvider;

    @Autowired 
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = getToken(request);
        if (token != null) {
            logger.info(" JWT detected: attempting validation");
        if (token != null && tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsername(token);
            logger.info(" Token validated successfully. Extracted username: {}", username);
            var userDetails = userDetailsService.loadUserByUsername(username);
            logger.info(" Loaded user details: {}", userDetails.getUsername());
            logger.info(" Authorities: {}", userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.info(" Authentication context updated for user: {}", username);

        }else {
        	   logger.warn("Invalid JWT token.");

        }
        }
        else {
            logger.warn(" No JWT token found in Authorization header.");
        }    
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
