package com.vdurmont.vdmail.mapper;

import com.vdurmont.vdmail.dto.EmailDTO;
import com.vdurmont.vdmail.dto.UserDTO;
import com.vdurmont.vdmail.model.Email;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class EmailMapper {
    @Inject private UserMapper userMapper;

    public EmailDTO generate(Email email) {
        EmailDTO dto = new EmailDTO();
        dto.setId(email.getId());
        dto.setCreatedDate(email.getCreatedDate());
        dto.setSubject(email.getSubject());
        dto.setContent(email.getContent());

        UserDTO sender = this.userMapper.generate(email.getSender());
        dto.setSender(sender);

        UserDTO recipient = this.userMapper.generate(email.getRecipient());
        dto.setRecipient(recipient);

        return dto;
    }
}
