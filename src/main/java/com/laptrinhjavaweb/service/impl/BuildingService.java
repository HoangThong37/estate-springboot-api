package com.laptrinhjavaweb.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.converter.BuildingConverter;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.BuildingDeleteRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.BuildingRepo;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.service.IBuildingService;
import com.laptrinhjavaweb.util.MapUtils;

import javassist.NotFoundException;

@Service
public class BuildingService implements IBuildingService {
	
	@Autowired
	private BuildingRepository buildingRepoCustom;

	@Autowired
	private BuildingRepo building;
	
	@Autowired
	private BuildingConverter buildingConverter;

	// cách buider
		@Override
		public List<BuildingSearchResponse> getBuildingList(Map<String, Object> requestParams, List<String> types) throws SQLException {
			List<BuildingSearchResponse> results = new ArrayList<>();
			BuildingSearchBuilder buildingSearchBuilder = convertParamToBuilder(requestParams, types);
			List<BuildingEntity> buildingEntities = buildingRepoCustom.findBuilding(buildingSearchBuilder);

			for (BuildingEntity item : buildingEntities) {
				BuildingSearchResponse response = buildingConverter.convertEntityToBuildingResponse(item);
				results.add(response);
			}
			return results;
		}

		private BuildingSearchBuilder convertParamToBuilder(Map<String, Object> requestParams, List<String> types) {
			try {
				BuildingSearchBuilder result = new BuildingSearchBuilder.Builder()
						.setName(MapUtils.getObject(requestParams, "name", String.class))
						.setFloorArea(MapUtils.getObject(requestParams, "floorarea", Integer.class))
						.setDistrict(MapUtils.getObject(requestParams, "districtid", Integer.class))
						.setWard(MapUtils.getObject(requestParams, "ward", String.class))
						.setStreet(MapUtils.getObject(requestParams, "street", String.class))
						.setNumberOfBasement(MapUtils.getObject(requestParams, "numberofbasement", Integer.class))
						.setDirection(MapUtils.getObject(requestParams, "direction", String.class))
						.setLevel(MapUtils.getObject(requestParams, "level", String.class))
						.setRentAreaFrom(MapUtils.getObject(requestParams, "rentareafrom", Integer.class))
						.setRentAreaTo(MapUtils.getObject(requestParams, "rentareato", Integer.class))
						.setRentPriceFrom(MapUtils.getObject(requestParams, "rentpricefrom", Integer.class))
						.setRentPriceTo(MapUtils.getObject(requestParams, "rentpriceto", Integer.class))
						.setManagerName(MapUtils.getObject(requestParams, "managername", String.class))
						.setStaffID(MapUtils.getObject(requestParams, "staffid", Integer.class))
						.setManagerPhone(MapUtils.getObject(requestParams, "managerphone", String.class))
						.setTypes(types)
						.build();
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		@Transactional
		public BuildingDTO createBuilding(BuildingDTO buildingDTO) {
	        BuildingEntity buildingEntity = buildingConverter.convertToEntityCustom(buildingDTO);
	        
	        BuildingDTO buildingDTOAfter = buildingConverter.convertToDTOCustom(building.save(buildingEntity));

			return buildingDTOAfter;
	    }

//		@Override
//		public void removeBuilding(List<Long> ids) {
//			if (ids != null) {
//				building.deleteByIdIn(ids);
//			}
//		}

		@Override
		@Transactional
		public void removeBuilding(BuildingDeleteRequest buildingDeleteRequest) throws NotFoundException {
			if (buildingDeleteRequest.getBuildingId() != null) {
				building.deleteByIdIn(buildingDeleteRequest.getBuildingId());
			}
		}

		@Override
		@Transactional
		public void removeOneBuilding(Long id) {
			try {
				if (id != null) {
					building.deleteById(id);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error delete one building in service");
			}
			
		}

//		@Override
//		public BuildingDTO findBuildingById(Long id) {
//			// TODO Auto-generated method stub
//			return null;
//		}
		
		@Override
		public BuildingDTO findBuildingById(Long id) {
	        if (id != null) {
	           // BuildingEntity buildingEntity = building.findById(id);
	           // BuildingDTO buildingDTO = buildingConverter.convertToDTO(buildingEntity);
	           // return buildingDTO;
	        }
	        return null;
	    }

		@Override
		@Transactional
		public BuildingDTO updateBuilding(BuildingDTO buildingDTO) {
			try {
				if (buildingDTO.getId() != null) {
					BuildingEntity buildingEntity = buildingConverter.convertToEntityCustom(buildingDTO); // trả ra cho dto
	                System.out.println("in ra type toa nha truoc khi convert to dto : " + buildingEntity.getTypes());
	                System.out.println("in ra getName toa nha truoc khi convert to dto : " + buildingEntity.getName());
	                System.out.println("in ra getDistrict toa nha truoc khi convert to dto : " + buildingEntity.getDistrict());
	                System.out.println("in ra getStreet toa nha truoc khi convert to dto : " + buildingEntity.getStreet());

					BuildingDTO buildingDTOAfter = buildingConverter.convertToDTOCustom(buildingEntity);
					return buildingDTOAfter;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error building update in service");
			}
	       return null;
		}

}