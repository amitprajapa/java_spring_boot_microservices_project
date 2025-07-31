package com.lcwd.user.service.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/user")
	public ResponseEntity<User> createUser(@RequestBody User user){
		
		User userl = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(userl);
	}
	
	@GetMapping("/{userId}")
	@CircuitBreaker(name ="ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId){
		User user = userService.getUser(userId);
		return ResponseEntity.ok(user);
	}
	
	//fall back method for circuitbreaker
	
	public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
		logger.info("Fallback is executed because service is down :",ex.getMessage());
		User user = User.builder()
				.email("dummy@gmail.com")
				.name("Dummy")
				.about("This user is dummy becasue some service is down")
				.userId("1321")
				.build();
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getAllUSers(){
		List<User> allUser = userService.getAllUSer();
		return ResponseEntity.ok(allUser);
	}

}
