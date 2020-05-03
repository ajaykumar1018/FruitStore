package com.fruitstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.fruitstore.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

	Role findByname(String name); 
	
}
