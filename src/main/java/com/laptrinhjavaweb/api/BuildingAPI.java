package com.laptrinhjavaweb.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.BuildingDeleteRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.dto.response.BuildingTypesResponse;
import com.laptrinhjavaweb.dto.response.StaffResponseDTO;
import com.laptrinhjavaweb.service.IBuildingService;
import com.laptrinhjavaweb.service.IBuildingTypeService;
import com.laptrinhjavaweb.service.IUserService;

import javassist.NotFoundException;

@RestController
@RequestMapping
public class BuildingAPI {
	
	@Autowired
	private IBuildingService buildingService;
	
    @Autowired
    private IUserService userService;

	@Autowired
	private IBuildingTypeService buildingTypeService;
	
	
	@GetMapping("/api/building")
    public List<BuildingSearchResponse> searchBuilding(@RequestParam(required = false) Map<String, Object> fieldSearch,
            @RequestParam(required = false) List<String> types) throws SQLException {
		return buildingService.getBuildingList(fieldSearch, types);
   }
	
    @PostMapping("/api/building")
    public BuildingDTO createBuilding(@RequestBody(required = false) BuildingDTO buildingDTO) {
        return buildingService.createBuilding(buildingDTO);
    }
    
    // delete one id
    @DeleteMapping("/api/building/{id}")
    public void deleteBuilding(@PathVariable("id") Long id) throws NotFoundException {
        buildingService.removeOneBuilding(id);
        
    }
    
    // delete n id
    @DeleteMapping("/api/building")
    public BuildingDeleteRequest deleteBuilding(@RequestBody BuildingDeleteRequest buildingDeleteRequest) throws NotFoundException {
        buildingService.removeBuilding(buildingDeleteRequest);
        return buildingDeleteRequest;
    }
    
    @PostMapping("/api/building/test")
    public List<BuildingTypesResponse> createTypes(@RequestBody(required = false) BuildingDTO buildingDTO) {
        return buildingTypeService.getAllByBuilding(buildingDTO);
    }
   
    
    // api load satff
    @GetMapping("/api/building/{id}/staff")
    public List<StaffResponseDTO> loadStaffByBuilding(@PathVariable("id") Long id) {
        return userService.finAllStaffByBuilding(id);
    }
    
    
    // api get loại tòa nhà
    @GetMapping("/api/building/type/{id}")
    public List<BuildingTypesResponse> findTypes(@PathVariable("id") Long id) {
       // return buildingTypeService.getAllByBuilding(buildingSer);
    	return null;
    }
    
    // update building
    @PutMapping("/api/building/buildingdto")
    public BuildingDTO updateBuilding(@RequestBody(required = false) BuildingDTO buildingdto) {
    	BuildingDTO buildingAfter = buildingService.updateBuilding(buildingdto);

        return buildingAfter;
    }
}
