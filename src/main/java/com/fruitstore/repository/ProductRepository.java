package com.fruitstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.fruitstore.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {


	
	
}
