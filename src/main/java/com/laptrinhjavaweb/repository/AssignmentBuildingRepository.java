package com.laptrinhjavaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.entity.AssignBuildingEntity;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.UserEntity;

public interface AssignmentBuildingRepository extends JpaRepository<AssignBuildingEntity, Long> {

    AssignBuildingEntity findByBuildingAndUser(BuildingEntity building, UserEntity user);
}
