package com.vdurmont.vdmail.controller;

import com.vdurmont.vdmail.dto.EmailDTO;
import com.vdurmont.vdmail.dto.UserDTO;
import com.vdurmont.vdmail.mapper.EmailMapper;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.service.LoginService;
import com.vdurmont.vdmail.service.EmailService;
import com.vdurmont.vdmail.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;

@Controller
@RequestMapping("emails")
public class EmailController {
    @Inject private EmailMapper emailMapper;
    @Inject private EmailService emailService;
    @Inject private LoginService loginService;
    @Inject private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public EmailDTO sendEmail(@RequestBody EmailDTO dto) {
        User user = this.loginService.getRequiredCurrentUser();
        User recipient = null;
        UserDTO recipientDTO = dto.getRecipient();
        if (recipientDTO != null) {
            recipient = this.userService.getOrCreate(recipientDTO.getAddress());
        }
        Email email = this.emailService.send(user, recipient, dto.getSubject(), dto.getContent());
        return this.emailMapper.generate(email);
    }
}
