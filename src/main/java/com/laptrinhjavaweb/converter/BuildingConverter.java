package com.laptrinhjavaweb.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.RentareaEntity;
import com.laptrinhjavaweb.enums.DistrictsEnum;
import com.laptrinhjavaweb.util.ValidateUtils;

@Component
public class BuildingConverter {

    @Autowired
    private ModelMapper modelMapper;

    public BuildingDTO convertToDTO(BuildingEntity entity) {
        BuildingDTO dto = modelMapper.map(entity, BuildingDTO.class);
        return dto;
    }

    public BuildingEntity convertToEntity(BuildingDTO dto) {
        BuildingEntity entity = modelMapper.map(dto, BuildingEntity.class);
        return entity;
    }

    // convert Entity-to-BuildingSearchResponse
    public BuildingSearchResponse convertEntityToBuildingResponse(BuildingEntity entity) {
        String districtName = "";
        String testName = entity.getDistrict();
        
        for (DistrictsEnum item : DistrictsEnum.values()) {
         	if (testName.equals(item.name())) {
        		districtName = item.getDistrictValue();
			}
        }
        String address = entity.getStreet() + " - " + entity.getWard() + " - " + districtName;
        BuildingSearchResponse response = modelMapper.map(entity, BuildingSearchResponse.class);
        response.setAddress(address);
        return response;
    }

    
    // tạo mới -> lưu
    // convert dto -> entity custom
    public BuildingEntity convertToEntityCustom(BuildingDTO dto) {
        BuildingEntity result = modelMapper.map(dto, BuildingEntity.class);

        if (dto.getTypes() != null) {
            String type = String.join(",", dto.getTypes());
            result.setTypes(type);
        }
        // xử lí type tòa nhà
//        String joinStr = "";
//        if (dto.getTypes() != null) {
//            for (String item : dto.getTypes()) { //tang_tret
//                joinStr += item + ",";
//            }
//            if (joinStr != null && !joinStr.isEmpty() && joinStr.endsWith(",")) {
//                joinStr = joinStr.substring(0, joinStr.length() - 1);
//            }
//            result.setTypes(joinStr);
//        }

        // xử lí rentarrea - input: 300,400,500-> string
        Set<RentareaEntity> rentareaEntities = new HashSet<>();
        if (dto.getAreaRent() != null) {
           String[] arrAreaRent = dto.getAreaRent().trim().split(","); // tách chuỗi qa dau ','
            for (String item : arrAreaRent) {
                RentareaEntity rentareaEntity = new RentareaEntity();
                rentareaEntity.setBuilding(result); //
                rentareaEntity.setValue(ValidateUtils.parseInteger(item));
                rentareaEntities.add(rentareaEntity);
            }
            result.setRentareas(rentareaEntities);
        }
        return result;
    }

    
 // convert entity -> dto custom
    public BuildingDTO convertToDTOCustom(BuildingEntity entity) {
        BuildingDTO result = modelMapper.map(entity, BuildingDTO.class);
        //System.out.println("Thong dep zai : " + entity.getTypes());

        // rent area
        List<String> rentareas = new ArrayList<>();
        if (entity.getRentareas() != null) {
            for (RentareaEntity itemRentArea : entity.getRentareas()) {
                rentareas.add(String.valueOf(itemRentArea.getValue()));
            }
            String rentArea = String.join(",", rentareas);  // tách = dấu phẩy
            result.setAreaRent(rentArea);
		}


        // types
        // db: NGUYEN_CAN, NOI_THAT
        if (entity.getTypes() != null) {
            List<String> types = new ArrayList<>();
            String[] arrTypes = entity.getTypes().trim().split(",");  // tách = dấu phẩy
            for (String item : arrTypes) {
                types.add(item);
            }
            result.setTypes(types);
        }
        return result;
    }


}