package com.signitives.teamslate.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.dto.LoginRequestDTO;
import com.test.dto.ProjectSummaryDTO;
import com.test.dto.RegistrationRequestDTO;
import com.test.entity.InviteToken;
import com.test.entity.Project;
import com.test.entity.User;
import com.test.service.impl.UserServiceImpl;
import com.test.service.interfaces.UserService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class UserController {
	
	private final Logger logger = LogManager.getLogger(UserController.class);
	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegistrationRequestDTO registrationRequest) {
	    
		System.out.println(registrationRequest);
		userService.register(registrationRequest);
		
	    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
	}
	
	@GetMapping("/getTokens")
    public ResponseEntity<List<InviteToken>> getAllInviteTokens() {
        List<InviteToken> tokens = userService.getAllTokens();
        return ResponseEntity.ok(tokens);
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
	    System.out.println(loginRequest);  // Debugging the received user
	    logger.info("loginRequest " + loginRequest);
	    String token = userService.login(loginRequest);
	    
	    if (token != null) {
	        return ResponseEntity.ok(Collections.singletonMap("token", token));  // Returning the token in JSON format
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	    }
	}
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping("/add")
    public ResponseEntity<Map<String, String>> addStock() {
		System.out.println("Inside Add");
		return null;
    	
        
        
    }
	
	@GetMapping("/users/{userId}/projects")
	public ResponseEntity<?> getProjectsForUser(@PathVariable("userId") UUID userId) {
	    List<ProjectSummaryDTO> projectSummary = userService.getProjectsForUser(userId);
	    return ResponseEntity.ok(projectSummary);
	}

  
	
	

}
