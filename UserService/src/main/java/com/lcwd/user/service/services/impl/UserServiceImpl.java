package com.lcwd.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
			
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HotelService hotelService;
	
	@Override
	public User saveUser(User user) {
		String id = UUID.randomUUID().toString();
		user.setUserId(id);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUSer() {
		List<User> user =  userRepository.findAll();
		for(User userl : user) {
			ArrayList<Rating> objcet = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+userl.getUserId(), ArrayList.class);
			logger.info("{}",objcet);
			userl.setRatings(objcet);
		}	
		return user;
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given id is not found on server "+userId));
		//fetch rating from Rating Service
		Rating[] objcet = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);
		logger.info("{}",objcet);
		
		List<Rating> ratings = Arrays.stream(objcet).toList();
		
		List<Rating> ratingList = ratings.stream().map(rating ->{
			//ResponseEntity<Hotel> forEntity= restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
			Hotel hotel = hotelService.getHotel(rating.getHotelId());
			rating.setHotel(hotel);
			return rating;
		}).collect(Collectors.toList());
		
		user.setRatings(ratingList);
		return user;
	}

}
