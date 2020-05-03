package com.fruitstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruitstore.domain.Product;
import com.fruitstore.repository.ProductRepository;
import com.fruitstore.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> findAll() {
		return (List<Product>) productRepository.findAll();
	}
	
	public Product findOne(Long id) {
		return productRepository.findById(id).get();
	}
}
