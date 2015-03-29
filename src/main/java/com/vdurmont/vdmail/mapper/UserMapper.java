package com.vdurmont.vdmail.mapper;

import com.vdurmont.vdmail.dto.UserDTO;
import com.vdurmont.vdmail.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDTO generate(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setName(user.getName());
        dto.setAddress(user.getAddress());
        return dto;
    }
}
