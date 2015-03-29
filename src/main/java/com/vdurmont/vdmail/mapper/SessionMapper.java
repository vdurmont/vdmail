package com.vdurmont.vdmail.mapper;

import com.vdurmont.vdmail.dto.SessionDTO;
import com.vdurmont.vdmail.dto.UserDTO;
import com.vdurmont.vdmail.model.Session;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SessionMapper {
    @Inject private UserMapper userMapper;

    public SessionDTO generate(Session session) {
        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setCreatedDate(session.getCreatedDate());
        dto.setExpirationDate(session.getExpirationDate());
        dto.setToken(session.getToken());

        UserDTO user = this.userMapper.generate(session.getUser());
        dto.setUser(user);

        return dto;
    }
}
