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
	public List<BuildingSearchResponse> getBuildingList(BuildingSearchRequest buildingSearchRequest) throws SQLException {
		List<BuildingSearchResponse> responses = new ArrayList<>();
		System.out.println("vô service rồi");
		for (BuildingEntity item : buildingRepository.findBuilding(buildingSearchRequest)) {
			DistrictEntity districtEntity = districtRepository.findDistrictById(item.getDistrictId());
			String districtName = districtEntity.getName();
			BuildingSearchResponse response = buildingConverter.convertEntityToBuildingResponse(item, districtName);
			responses.add(response);
		}
		return responses;
	}

	@Override
	public BuildingDTO save(BuildingDTO newBuilding) {
		// TODO Auto-generated method stub
		return null;
	}

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
