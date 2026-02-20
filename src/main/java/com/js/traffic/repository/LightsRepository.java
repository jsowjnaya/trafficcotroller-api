package com.js.traffic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.js.traffic.model.Lights;
/**
 * A JPA repository for Lights model
 */
@Repository
public interface LightsRepository extends JpaRepository<Lights, Long> {

    List<Lights> findByJid(Integer jid);

    List<Lights> findByDirection(String direction); 
    
    List<Lights> findByCodeAndJid(String code,Integer jid);
    
    List<Lights> findByColorAndJid(String cocolor,Integer jid); 
    
  
}

