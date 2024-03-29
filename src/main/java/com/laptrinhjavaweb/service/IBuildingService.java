package com.laptrinhjavaweb.service;

import java.util.List;
import java.util.Map;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.AssignmentBuildingRequest;
import com.laptrinhjavaweb.dto.request.BuildingDeleteRequest;
import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;

import javassist.NotFoundException;

public interface IBuildingService {
	//Map<String, String> getDistricts();
	//Map<String, String> getBuildingTypes();
    List<BuildingDTO> findAll();
    BuildingDTO createBuilding(BuildingDTO buildingDTO);
    BuildingDTO findBuildingById(Long id);
    Map<String, String> getBuildingTypes();

    void assignmentBuilding(AssignmentBuildingRequest assignmentBuildingRequest, Long buildingId);
    //void assignmentBuilding(List<Long> listUserId, Long buildingId);
    void removeBuilding(BuildingDeleteRequest buildingDeleteRequest) throws NotFoundException;
    List<BuildingSearchResponse> findAll(BuildingSearchRequest buildingSearchRequest);
    BuildingDTO updateBuilding(BuildingDTO buildingDTO);

}
