package com.laptrinhjavaweb.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.dto.response.StaffResponseDTO;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.repository.UserRepository;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;

    public UserDTO convertToDto (UserEntity entity){
        UserDTO result = modelMapper.map(entity, UserDTO.class);
        return result;
    }

    public List<StaffResponseDTO> convertToStaffAssignmentReponse(List<UserEntity> staffAssignments) {
//        List<StaffResponseDTO> result = new ArrayList<>();
//        for (UserEntity item : userRepository.getAllStaff()) {
//            int i = 0;
//            for (UserEntity item1 : staffAssignments) {
//                if (item.getId() == item1.getId()) {
//                    i++;
//                }
//            }
//            StaffResponseDTO staffAssignmentReponse = modelMapper.map(item, StaffResponseDTO.class);
//            if (i > 0) {
//                staffAssignmentReponse.setChecked("checked");
//                result.add(staffAssignmentReponse);
//            }
//        }
//        return result;
       
        List<StaffResponseDTO> result = new ArrayList<>();
        for (UserEntity staffAll : userRepository.getAllStaff()) {
        	StaffResponseDTO listStaff = modelMapper.map(staffAll, StaffResponseDTO.class);
            for (UserEntity userList : staffAssignments) {
                if (staffAll.getId() == userList.getId()) {
                    listStaff.setChecked("checked");
                }
            }
            result.add(listStaff);
        }
        return result;
    }

    public UserEntity convertToEntity (UserDTO dto){
        UserEntity result = modelMapper.map(dto, UserEntity.class);
        return result;
    }
}
