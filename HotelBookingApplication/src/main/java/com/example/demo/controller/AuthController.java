package com.example.demo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;



@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired 
    private AuthenticationManager authManager;
    @Autowired 
    private JwtTokenProvider tokenProvider;	
    @Autowired 
    private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
    	logger.info("Entering The User Credentials ");
        User user = userService.register(body.get("username"), body.get("password"));
        logger.info("User Registered Successfully With "+user.getUsername());
        return ResponseEntity.ok(user);  
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
    	logger.info("Enter the Username and Password to LOGIN ");
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(body.get("username"), body.get("password")));
        String token = tokenProvider.generateToken(body.get("username"));
        logger.info("Authenticated Successfully with entered Username and Password ");
        return ResponseEntity.ok(Map.of("token", token));
    }
}

