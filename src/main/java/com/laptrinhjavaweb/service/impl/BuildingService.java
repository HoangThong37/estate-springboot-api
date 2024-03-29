package com.laptrinhjavaweb.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.converter.BuildingConverter;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.AssignmentBuildingRequest;
import com.laptrinhjavaweb.dto.request.BuildingDeleteRequest;
import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.enums.BuildingTypesEnum;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.repository.UserRepository;
import com.laptrinhjavaweb.service.IBuildingService;

import javassist.NotFoundException;

@Service
public class BuildingService implements IBuildingService {

//	@Autowired
//	private BuildingRepositoryCustom buildingRepoCustom;
	
	@Autowired
	private BuildingRepository buildingRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BuildingConverter buildingConverter;


	@Override
	public List<BuildingDTO> findAll() {
		List<BuildingDTO> result = new ArrayList<>();
		List<BuildingEntity> buildingEntities = buildingRepository.findAll();
		for(BuildingEntity item : buildingEntities) {
			BuildingDTO buildingDTO = buildingConverter.convertToDTO(item);
			result.add(buildingDTO);
		}
		return result;
	}

	@Override
	@Transactional
	public BuildingDTO createBuilding(BuildingDTO buildingDTO) {
		BuildingEntity buildingEntity = buildingConverter.convertToEntityCustom(buildingDTO);

		BuildingDTO buildingDTOAfter = buildingConverter.convertToDTOCustom(buildingRepository.save(buildingEntity));

		return buildingDTOAfter;
    }


	@Override
	public BuildingDTO findBuildingById(Long id) {
        if (id != null) {
            BuildingEntity buildingEntity = buildingRepository.findById(id);
            BuildingDTO buildingDTO = buildingConverter.convertToDTOCustom(buildingEntity);
            return buildingDTO;
        }
        return null;
    }

	@Override
	public Map<String, String> getBuildingTypes() {
		Map<String, String> buildingTypes = new HashMap<>();
		for (BuildingTypesEnum item : BuildingTypesEnum.values()) {
			buildingTypes.put(item.toString(), item.getBuildingTypeValue());
		}
		return buildingTypes;
	}



	@Override
	public List<BuildingSearchResponse> findAll(BuildingSearchRequest buildingSearchRequest) {
		List<BuildingSearchResponse> result = new ArrayList<>();
		BuildingSearchBuilder buildingSearchBuilder = convertParamToBuilder(buildingSearchRequest);
		//List<BuildingEntity> buildingEntities;
		try {
		
			List<BuildingEntity> buildingEntities = buildingRepository.findBuilding(buildingSearchBuilder);
			for (BuildingEntity item : buildingEntities) {
				result.add(buildingConverter.convertEntityToBuildingResponse(item));
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	@Transactional
	public BuildingDTO updateBuilding(BuildingDTO buildingDTO) {
		//System.out.println("Thay doi cai ten : " + buildingDTO.getName());
		//System.out.println("Thay doi type : " + buildingDTO.getTypes());

		try {
			if (buildingDTO.getId() != null) {
				BuildingEntity buildingEntity = buildingConverter.convertToEntityCustom(buildingDTO); // trả ra cho dto

				BuildingDTO buildingDTOAfter = buildingConverter.convertToDTOCustom(buildingRepository.save(buildingEntity));
				return buildingDTOAfter;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error building update in service");
		}
       return null;
	}



	private BuildingSearchBuilder convertParamToBuilder(BuildingSearchRequest buildingSearchRequest) {
		try {
			BuildingSearchBuilder result = new BuildingSearchBuilder.Builder()
					.setName(buildingSearchRequest.getName())
					.setFloorArea(buildingSearchRequest.getFloorArea())
					.setDistrict(buildingSearchRequest.getDistrictCode())
					.setWard(buildingSearchRequest.getWard())
					.setStreet(buildingSearchRequest.getStreet())
					.setNumberOfBasement(buildingSearchRequest.getNumberOfBasement())
					.setDirection(buildingSearchRequest.getDirection())
					.setLevel(buildingSearchRequest.getLevel())
					.setRentAreaFrom(buildingSearchRequest.getRentAreaFrom())
					.setRentAreaTo(buildingSearchRequest.getRentAreaTo())
					.setRentPriceFrom(buildingSearchRequest.getRentPriceFrom())
					.setRentPriceTo(buildingSearchRequest.getRentPriceTo())
					.setManagerName(buildingSearchRequest.getManagerName())
					.setManagerPhone(buildingSearchRequest.getManagerPhone())
					.setStaffID(buildingSearchRequest.getStaffId())
					.setTypes(buildingSearchRequest.getTypes())
					.build();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	// test
	@Override
	public void assignmentBuilding(AssignmentBuildingRequest assignmentBuildingRequest, Long buildingID) {
		List<UserEntity> userEntities = new ArrayList<>();
		for (Integer item : assignmentBuildingRequest.getStaffIds()) {
			userEntities.add(userRepository.findOnedById(item.longValue()));
		}
		BuildingEntity buildingEntity = buildingRepository.findById(buildingID);
		buildingRepository.assignmentBuilding(userEntities, buildingEntity);

	}


	@Override
	@Transactional
	public void removeBuilding(BuildingDeleteRequest buildingDeleteRequest) throws NotFoundException {
		if (buildingDeleteRequest.getBuildingId() != null) {
			buildingRepository.deleteByIdIn(buildingDeleteRequest.getBuildingId());
		}
	}
}
