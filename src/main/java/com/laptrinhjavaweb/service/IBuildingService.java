package com.laptrinhjavaweb.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.BuildingDeleteRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;

import javassist.NotFoundException;

public interface IBuildingService {
	//Map<String, String> getDistricts();
	//Map<String, String> getBuildingTypes();
	
	BuildingDTO save(BuildingDTO buildingDTO);
    List<BuildingSearchResponse> getBuildingList(Map<String, Object> fieldSearch, List<String> types) throws SQLException;
	
    void removeOneBuilding(Long id);
    void removeBuilding(BuildingDeleteRequest buildingDeleteRequest) throws NotFoundException;
   // BuildingDTO updateBuilding(BuildingDTO buildingDTO);
}
