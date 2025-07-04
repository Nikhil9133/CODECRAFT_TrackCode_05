package com.project.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	

	 Optional<User> findByUsername(String username);


}
