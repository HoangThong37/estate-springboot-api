package com.laptrinhjavaweb.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.entity.BuildingEntity;

public interface BuildingRepository {
    List<BuildingEntity> findBuilding(Map<String, String> buildingSearchRequest, List<String> types) throws SQLException;
}

//extends JpaRepository<BuildingEntity, Long>