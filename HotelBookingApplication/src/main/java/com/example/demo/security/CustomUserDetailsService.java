package com.example.demo.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired 
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	 logger.info(" Attempting to load user: {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> 
            new UsernameNotFoundException("User not found"));
        
        logger.info(" User '{}' found. Role: {}", user.getUsername(), user.getRole());
       
        var authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        logger.info(" Assigned Authorities: {}", authorities);

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole())));
    }
}

