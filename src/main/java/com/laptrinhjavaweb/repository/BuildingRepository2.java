package com.laptrinhjavaweb.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.laptrinhjavaweb.entity.BuildingEntity;

public interface BuildingRepository2 {
	List<BuildingEntity> findBuilding2(Map<String, Object> buildingSearchRequest, List<String> types) throws SQLException;
}