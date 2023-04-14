package com.laptrinhjavaweb.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.converter.BuildingConverter;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.DistrictEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;
import com.laptrinhjavaweb.enums.BuildingTypesEnum;
import com.laptrinhjavaweb.enums.DistrictsEnum;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.repository.BuildingRepository2;
import com.laptrinhjavaweb.repository.DistrictRepository;
import com.laptrinhjavaweb.repository.RentAreaRepository;
import com.laptrinhjavaweb.service.IBuildingService;
import com.laptrinhjavaweb.util.MapUtils;

@Service
public class BuildingService implements IBuildingService {

	@Autowired
	private BuildingConverter buildingConverter;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private BuildingRepository2 buildingRepository2;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;
	
	@Override
	public Map<String, String> getDistricts() {
		Map<String, String> districts = new HashMap<>();
		for (DistrictsEnum item: DistrictsEnum.values()) {
			districts.put(item.toString(), item.getDistrictValue());// item ở đây là : QUAN_1 / item.getDistrictValue() : giá trị của Q_1 là quận 1
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
		List<BuildingEntity> buildingEntities = buildingRepository.findBuilding(fieldSearch, types);
		
		for (BuildingEntity item : buildingEntities) {
			
			DistrictEntity districtEntity = districtRepository.findDistrictById(item.getDistrictId());
			String districtName = districtEntity.getName();

			List<RentAreaEntity> rentArea = rentAreaRepository.findRentArea(item.getRentAreaId());
				
			BuildingSearchResponse response = buildingConverter.convertEntityToBuildingResponse(item, districtName, rentArea);
			responses.add(response);
		}
		return responses;
	}

	
	
	// cách 2
    // search building by field 
	@Override
	public List<BuildingSearchResponse> getBuildingList2(Map<String, Object> fieldSearch, List<String> types) throws SQLException {
		List<BuildingSearchResponse> responses = new ArrayList<>();
		List<BuildingEntity> buildingEntities = buildingRepository2.findBuilding2(fieldSearch, types);
		for (BuildingEntity item : buildingEntities) {
			DistrictEntity districtEntity = districtRepository.findDistrictById(item.getDistrictId());
			String districtName = districtEntity.getName();
			List<RentAreaEntity> rentArea = rentAreaRepository.findRentArea(item.getRentAreaId());
			BuildingSearchResponse response = buildingConverter.convertEntityToBuildingResponse(item, districtName, rentArea);
			responses.add(response);
		}
		return responses;
	}

	
	
	// cách buider
	@Override
	public List<BuildingSearchResponse> getBuildingList3(Map<String, Object> requestParams, List<String> types) throws SQLException {
		List<BuildingSearchResponse> results = new ArrayList<>();
		BuildingSearchBuilder buildingSearchBuilder = convertToBuildingSearchBuilder(requestParams, types);
		List<BuildingEntity> buildingEntities = buildingRepository2.findBuilding3(buildingSearchBuilder);
		
		for (BuildingEntity item : buildingEntities) {
			DistrictEntity districtEntity = districtRepository.findDistrictById(item.getDistrictId());
			String districtName = districtEntity.getName();
			List<RentAreaEntity> rentArea = rentAreaRepository.findRentArea(item.getRentAreaId());
			BuildingSearchResponse response = buildingConverter.convertEntityToBuildingResponse(item, districtName, rentArea);
			results.add(response);
		} 
		return results;
	}

	private BuildingSearchBuilder convertToBuildingSearchBuilder(Map<String, Object> requestParams, List<String> types) {
		BuildingSearchBuilder result = new BuildingSearchBuilder.Builder()
				                      .setName(MapUtils.getObject(requestParams, "name", String.class))
				                      .setFloorArea(MapUtils.getObject(requestParams, "floorarea", Integer.class))
				                      .setDistrict(MapUtils.getObject(requestParams, "districtId", Integer.class))
				                      .setStreet(MapUtils.getObject(requestParams, "street", String.class))
				                      .setWard(MapUtils.getObject(requestParams, "ward", String.class))
				                      .setNumberOfBasement(MapUtils.getObject(requestParams, "numberofbasement", Integer.class))
				                      .setCostRentFrom(MapUtils.getObject(requestParams, "rentPriceFrom", Integer.class))
				                      .setCostRentTo(MapUtils.getObject(requestParams, "rentPriceTo", Integer.class))  
				                     .setAreaRentFrom(MapUtils.getObject(requestParams, "rentAreaFrom", Integer.class))	  
				                     .setAreaRentTo(MapUtils.getObject(requestParams, "rentAreaTo", Integer.class))
				                     .setStaffId(MapUtils.getObject(requestParams, "staffid", Integer.class))  
				                     .setBuildingTypes(MapUtils.getObject(requestParams, "rentPriceTo", String[].class))
				                     .build();
		   return result;
	}

}
