package com.laptrinhjavaweb.repository;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.entity.DistrictEntity;


public interface DistrictRepository {
	 DistrictEntity findDistrictById(Long id) throws SQLException;
}
