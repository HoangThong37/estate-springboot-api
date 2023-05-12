package com.laptrinhjavaweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.laptrinhjavaweb.entity.BuildingEntity;

public interface BuildingRepo extends JpaRepository<BuildingEntity, Long>,CrudRepository<BuildingEntity, Long> {
	
	//BuildingEntity findById(Long id);
	void deleteByIdIn(List<Long> ids);
	

	
}
