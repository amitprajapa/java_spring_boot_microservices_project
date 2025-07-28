package com.rating.services;

import java.util.List;

import com.rating.entities.Rating;

public interface RatingService {
	//create
	
	Rating create(Rating rating);
	
	
	//get all ratings
	List<Rating> getRatings();
	
	
	//
	List<Rating> getRatingsByUserId(String userId);
	
	List<Rating> getRatingsByHotelId(String hotelId);

}
