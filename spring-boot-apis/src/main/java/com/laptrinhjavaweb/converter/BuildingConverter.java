package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.entity.BuildingEntity;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;

@Component
public class BuildingConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingDTO convertToDTO(BuildingEntity entity) {
		BuildingDTO dto = modelMapper.map(entity, BuildingDTO.class);
		//dto.setAddress(dto.getStreet() +", "+ dto.getWard() +", "+dto.getDistrict());
		return dto;
	}
	
	public BuildingEntity convertToEntity(BuildingDTO dto) {
		BuildingEntity entity = modelMapper.map(dto, BuildingEntity.class);
		return entity;
	}
	
	public BuildingDTO convertToDTOs(List<BuildingEntity> entity) {
		BuildingDTO dto = modelMapper.map(entity, BuildingDTO.class);
		return dto;
	}
	
	// convert Entity-to-BuildingSearchResponse
	public BuildingSearchResponse convertEntityToBuildingResponse(BuildingEntity entity) {
		String address = entity.getStreet() + " - " + entity.getWard() + " - " +  entity.getDistrictId();
		BuildingSearchResponse response = modelMapper.map(entity, BuildingSearchResponse.class);
		response.setAddress(address);
		
		return response;
	}

}
