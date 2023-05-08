package com.laptrinhjavaweb.repository;

import java.sql.SQLException;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity;

public interface BuildingRepository  {
	
    List<BuildingEntity> findBuilding(BuildingSearchBuilder builder) throws SQLException;

}
