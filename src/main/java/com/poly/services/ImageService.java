package com.poly.services;

import java.util.List;

import com.poly.model.Image;



public interface ImageService {

	List<Image> findByTourId(Long id);

	public Image addToTour(Long tourId,String url);

	void deleteById(Long id);
}
