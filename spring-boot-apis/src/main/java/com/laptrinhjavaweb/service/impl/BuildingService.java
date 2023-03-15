package com.laptrinhjavaweb.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.converter.BuildingConverter;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.DistrictEntity;
import com.laptrinhjavaweb.enums.BuildingTypesEnum;
import com.laptrinhjavaweb.enums.DistrictsEnum;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.repository.DistrictRepository;
import com.laptrinhjavaweb.service.IBuildingService;
import com.laptrinhjavaweb.util.ValidateUtil;

@Service
public class BuildingService implements IBuildingService {

	@Autowired
	private BuildingConverter buildingConverter;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Override
	public Map<String, String> getDistricts() {
		Map<String, String> districts = new HashMap<>();
		for (DistrictsEnum item: DistrictsEnum.values()) {
			districts.put(item.toString(), item.getDistrictValue());
		}
		return districts;
	}

	@Override
	public Map<String, String> getBuildingTypes() {
		Map<String, String> buildingTypes = new HashMap<>();
		for (BuildingTypesEnum item: BuildingTypesEnum.values()) {
			buildingTypes.put(item.toString(), item.getBuildingTypeValue());
		}
		return buildingTypes;
	}


    // search building by field 
	// return building response
	@Override
	public List<BuildingSearchResponse> getBuildingList(Map<String, String> fieldSearch, List<String> types) throws SQLException {
		List<BuildingSearchResponse> responses = new ArrayList<>();
		System.out.println("vô service rồi");
		 // BuildingSearchRequest request = searchFieldParams(fieldSearch);
		List<BuildingEntity> buildingEntities = buildingRepository.findBuilding(fieldSearch, types);
		
		for (BuildingEntity item : buildingEntities) {
			DistrictEntity districtEntity = districtRepository.findDistrictById(item.getDistrictId());
			String districtName = districtEntity.getName();
			BuildingSearchResponse response = buildingConverter.convertEntityToBuildingResponse(item, districtName);
			responses.add(response);
		}
		return responses;
	}
	

/*	// client search request
    // 
	private BuildingSearchRequest searchFieldParams(Map<String, String> fieldSearch) {
		BuildingSearchRequest searchRequest = new BuildingSearchRequest();
        try {
    		searchRequest.setName((String) ValidateUtil.validateSearch(fieldSearch.get("name")));
    		searchRequest.setWard((String) ValidateUtil.validateSearch(fieldSearch.get("ward")));
    		searchRequest.setStreet((String) ValidateUtil.validateSearch(fieldSearch.get("street")));
    		searchRequest.setDirection((String) ValidateUtil.validateSearch(fieldSearch.get("direction")));
    		searchRequest.setLevel((String) ValidateUtil.validateSearch(fieldSearch.get("level")));
    		searchRequest.setFloorArea((Integer) ValidateUtil.validateSearch(fieldSearch.get("floorarea")));
    		searchRequest.setNumberOfBasement((Integer) ValidateUtil.validateSearch(fieldSearch.get("numberofbasement")));
    		searchRequest.setRentAreaFrom((Integer) ValidateUtil.validateSearch(fieldSearch.get("rentAreaFrom")));
    		searchRequest.setRentAreaTo((Integer) ValidateUtil.validateSearch(fieldSearch.get("rentAreaTo")));
    		searchRequest.setRentPriceFrom((Integer) ValidateUtil.validateSearch(fieldSearch.get("rentPriceFrom")));
    		searchRequest.setRentPriceTo((Integer) ValidateUtil.validateSearch(fieldSearch.get("rentPriceTo")));
    		searchRequest.setStaffId((Integer) ValidateUtil.validateSearch(fieldSearch.get("staffId")));  // nv quản lí -gửi id
    		searchRequest.setDistrictCode((String) ValidateUtil.validateSearch(fieldSearch.get("districtCode"))); // districtCode
    		
        } catch (Exception e) {
        	System.out.println("Error service search request");
			e.printStackTrace();
		}
		return searchRequest;
	}*/



/*	@Override
	@Transactional
	public BuildingDTO save(BuildingDTO newBuilding) {
		BuildingEntity buildingEntity = buildingConverter.convertToEntity(newBuilding);
		return buildingConverter.convertToDTO(buildingRepository.save(buildingEntity));
	}*/

/*	@Override
	public List<BuildingDTO> getBuildingList() {
		List<BuildingEntity> listEntity = buildingRepository.findAll(); // all list enttity
		List<BuildingDTO> listDto = new ArrayList<>();
		for (BuildingEntity item : listEntity) {
			BuildingDTO dto = new BuildingDTO();
			dto.setName(item.getName());
			dto.setNumberOfBasement(String.valueOf(item.getNumberOfBasement()));
			listDto.add(dto);
		}
		return listDto;
	}*/
	
	
}
