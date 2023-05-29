package com.laptrinhjavaweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.custom.BuildingRepositoryCustom;


public interface BuildingRepository extends BuildingRepositoryCustom, JpaRepository<BuildingEntity, Long> {
    BuildingEntity findById(Long id);
    void deleteByIdIn(List<Long> ids);
}
