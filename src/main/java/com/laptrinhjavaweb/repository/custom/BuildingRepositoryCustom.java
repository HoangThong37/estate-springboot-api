package com.laptrinhjavaweb.repository.custom;

import java.sql.SQLException;
import java.util.List;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.UserEntity;

public interface BuildingRepositoryCustom  {
	
    List<BuildingEntity> findBuilding(BuildingSearchBuilder builder) throws SQLException;
    void assignmentBuilding(List<UserEntity> userEntities, BuildingEntity buildingEntity);
}
