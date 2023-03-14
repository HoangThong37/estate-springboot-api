package com.laptrinhjavaweb.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.service.IBuildingService;

@RestController
@RequestMapping
public class BuildingAPI {
	
	@Autowired
	private IBuildingService buildingService;
	
//	@PostMapping("/api/building")
//	public BuildingDTO createBuilding(@RequestBody BuildingDTO newBuilding) {
//		return buildingService.save(newBuilding);
//	}
	
	@GetMapping("/api/building")
	public List<BuildingSearchResponse> searchBuilding(@RequestParam(required = false) Map<String, Object> fieldSearch) 
			                            throws SQLException {
		return buildingService.getBuildingList(fieldSearch);
	}

	
	
//	@GetMapping("/api/building")
//	public List<BuildingSearchResponse> searchBuilding(@RequestParam(value = "name", required = false) String name,
//			                                @RequestParam(value = "ward", required = false) String ward, // phường
//			                                @RequestParam(value = "street", required = false) String street, // đường
//			                                @RequestParam(value = "direction", required = false) String direction, // hướng
//			                                @RequestParam(value = "level", required = false) String level, // level			                                
//			                                @RequestParam(value = "floorarea", required = false) String floorArea,
//			                                @RequestParam(value = "numberofbasement", required = false) Integer numberOfBasement,
//			                                @RequestParam(value = "rentAreaFrom", required = false) Integer rentAreaFrom,
//			                                @RequestParam(value = "rentAreaTo", required = false) Integer rentAreaTo,
//			                                @RequestParam(value = "rentPriceFrom", required = false) Integer rentPriceFrom,
//			                                @RequestParam(value = "rentPriceTo", required = false) Integer rentPriceTo,
//			                                @RequestParam(value = "staffId", required = false) Integer staffId, // nv quản lí -gửi id
//			                                @RequestParam(value = "districtCode", required = false) String districtCode
//			                               // @RequestParam(value = "ward", required = false) String ward  
//			                                ) throws SQLException {
//		BuildingSearchRequest request = new BuildingSearchRequest();
//		request.setName(name);
//		request.setWard(ward);
//		request.setStreet(street);  // address
//		request.setLevel(level);
//		request.setFloorArea(floorArea); // dtich sàn
//		request.setNumberOfBasement(numberOfBasement); // số tầng hầm
//		request.setStreet(street);
//		request.setWard(ward);  // address
//		request.setRentAreaFrom(rentAreaFrom);
//		request.setRentAreaFrom(rentAreaTo);
//		request.setRentPriceFrom(rentPriceFrom);
//		request.setRentPriceTo(rentPriceTo);
//		request.setStaffId(staffId);
//		request.setDistrictCode(districtCode); // address 
//		
//	
//		List<BuildingSearchResponse> responses = buildingService.getBuildingList(request);
//        return responses;
//	}
}
