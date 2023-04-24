package com.laptrinhjavaweb.repository;

import java.sql.SQLException;
import java.util.List;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity1;

public interface BuildingRepository3 {
	
	// sử dụng buider
    List<BuildingEntity1> findBuilding3(BuildingSearchBuilder builder)  throws SQLException ;

}
