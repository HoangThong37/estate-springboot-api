package com.laptrinhjavaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.converter.UserConverter;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.dto.response.StaffResponseDTO;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.repository.impl.UserRepositoryImpl;
import com.laptrinhjavaweb.service.IUserService;

@Service
public class UserService implements IUserService {

  
    @Autowired
    private UserRepositoryImpl userRepoCustom;

    @Autowired
    private UserConverter userConverter;


    @Override
    public List<UserDTO> getAllStaff() {
        List<UserDTO> result = new ArrayList<>();
         List<UserEntity> listStaff = userRepoCustom.getAllStaff();
        for (UserEntity item : listStaff) {
            // convert từ entity qua dto
            UserDTO userDTO  = userConverter.convertToDto(item);
            result.add(userDTO);
        }
        return result;
    }

    @Override
    public List<StaffResponseDTO> finAllStaffByBuilding(Long id) {
       
        return userConverter.convertToStaffAssignmentReponse(userRepoCustom.getAllStaffByBuilding(id));
    }

}
