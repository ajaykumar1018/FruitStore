package com.fruitstore.service;


import java.util.List;

import com.fruitstore.domain.Product;

public interface ProductService {
	List<Product> findAll ();
	
	Product findOne(Long id);
}
