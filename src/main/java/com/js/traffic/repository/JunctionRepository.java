package com.js.traffic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.js.traffic.model.Junction;

/**
 * A JPA repository for Junction model
 */
@Repository
public interface JunctionRepository extends JpaRepository<Junction, Integer>{
	

}
