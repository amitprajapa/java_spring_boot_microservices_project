package com.hotel.services;

import java.util.List;

import com.hotel.entites.Hotel;

public interface HotelService {
	
	Hotel create(Hotel hotel);
	
	List<Hotel> getAll();
	
	Hotel get(String id);

}
