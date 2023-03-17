package com.laptrinhjavaweb.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;

import antlr.StringUtils;

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
	public BuildingSearchResponse convertEntityToBuildingResponse(BuildingEntity entity, String districtName, List<RentAreaEntity> rentArea) {
		String address = entity.getStreet() + " - " + entity.getWard() + " - " +  districtName;
         
		String joinString = "";
		//List<Integer> joinString = new ArrayList<>();
		//StringBuilder list = new StringBuilder();
		for (RentAreaEntity item : rentArea) {
			if (item.getBuildingid() == entity.getId()) {
				joinString += item.getValue().toString() + "," ;
			}
		}
	    if (joinString != null && joinString.charAt(joinString.length() - 1) == ',') {
	    	joinString = joinString.substring(0, joinString.length() - 1);
	    }
		BuildingSearchResponse response = modelMapper.map(entity, BuildingSearchResponse.class);
		response.setAddress(address);
	    response.setRentArea(joinString);
		//response.setRentArea(list);
		
		return response;
	}

}
