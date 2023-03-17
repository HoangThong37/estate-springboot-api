package com.laptrinhjavaweb.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.converter.BuildingConverter;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.DistrictEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;
import com.laptrinhjavaweb.enums.BuildingTypesEnum;
import com.laptrinhjavaweb.enums.DistrictsEnum;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.repository.DistrictRepository;
import com.laptrinhjavaweb.repository.RentAreaRepository;
import com.laptrinhjavaweb.service.IBuildingService;

@Service
public class BuildingService implements IBuildingService {

	@Autowired
	private BuildingConverter buildingConverter;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;
	
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
	@Override
	public List<BuildingSearchResponse> getBuildingList(Map<String, String> fieldSearch, List<String> types) throws SQLException {
		List<BuildingSearchResponse> responses = new ArrayList<>();
		System.out.println("vô service rồi");
		 // BuildingSearchRequest request = searchFieldParams(fieldSearch);
		List<BuildingEntity> buildingEntities = buildingRepository.findBuilding(fieldSearch, types);
		
		for (BuildingEntity item : buildingEntities) {
			
			DistrictEntity districtEntity = districtRepository.findDistrictById(item.getDistrictId());
			String districtName = districtEntity.getName();
			
			// rentArea 
			List<RentAreaEntity> rentArea = rentAreaRepository.findRentArea(item.getRentAreaId());
				
			BuildingSearchResponse response = buildingConverter.convertEntityToBuildingResponse(item, districtName, rentArea);
			responses.add(response);
		}
		return responses;
	}
	
}
