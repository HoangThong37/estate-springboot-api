package com.laptrinhjavaweb.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.service.IBuildingService;

@RestController
@RequestMapping
public class BuildingAPI {
	
	@Autowired
	private IBuildingService buildingService;

	
//	@GetMapping("/api/building")
//	public List<BuildingSearchResponse> searchBuilding(@RequestParam(required = false) Map<String, String> fieldSearch,
//			                                           @RequestParam(required = false) List<String> types) 
//			                            throws SQLException {
//		return buildingService.getBuildingList(fieldSearch, types);
//	}
	
	//cách 2: sd toán tử 3 ngôi
	@GetMapping("/api/building")
	public List<BuildingSearchResponse> searchBuilding2(@RequestParam(required = false) Map<String, String> fieldSearch,
			                                           @RequestParam(required = false) List<String> types) 
			                            throws SQLException {
		return buildingService.getBuildingList(fieldSearch, types);
	}

}
