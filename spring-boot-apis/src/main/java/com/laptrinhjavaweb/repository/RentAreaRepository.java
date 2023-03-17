package com.laptrinhjavaweb.repository;

import java.sql.SQLException;
import java.util.List;

import com.laptrinhjavaweb.entity.DistrictEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;

public interface RentAreaRepository {
	List<RentAreaEntity> findRentArea(Integer id) throws SQLException;
	
}
