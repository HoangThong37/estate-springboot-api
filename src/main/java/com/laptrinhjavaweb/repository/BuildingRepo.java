package com.laptrinhjavaweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.entity.BuildingEntity;

public interface BuildingRepo extends JpaRepository<BuildingEntity, Long> {
	
	//BuildingEntity findById(Long id);
	void deleteByIdIn(List<Long> ids);

	
}
