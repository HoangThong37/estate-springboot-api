package com.laptrinhjavaweb.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;

public interface IBuildingService {
	Map<String, String> getDistricts();
	Map<String, String> getBuildingTypes();
	
	List<BuildingSearchResponse> getBuildingList(Map<String, String> fieldSearch, List<String> types) throws SQLException;
	
	
	List<BuildingSearchResponse> getBuildingList2(Map<String, Object> fieldSearch, List<String> types) throws SQLException;
}
