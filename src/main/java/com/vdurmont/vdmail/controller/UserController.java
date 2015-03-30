package com.vdurmont.vdmail.controller;

import com.vdurmont.vdmail.dto.EmailDTO;
import com.vdurmont.vdmail.dto.UserDTO;
import com.vdurmont.vdmail.mapper.EmailMapper;
import com.vdurmont.vdmail.mapper.UserMapper;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.service.EmailService;
import com.vdurmont.vdmail.service.LoginService;
import com.vdurmont.vdmail.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("users")
public class UserController {
    @Inject private EmailMapper emailMapper;
    @Inject private EmailService emailService;
    @Inject private LoginService loginService;
    @Inject private UserMapper userMapper;
    @Inject private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public UserDTO createUser(@RequestBody UserDTO dto) {
        User user = this.userService.create(dto.getName(), dto.getAddress(), dto.getPassword());
        return this.userMapper.generate(user);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ResponseBody
    public UserDTO getUser(@PathVariable int userId) {
        User user = this.userService.getById(userId);
        this.loginService.assertHasRightOn(user);
        return this.userMapper.generate(user);
    }

    @RequestMapping(value = "{userId}/contacts", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDTO> getContacts(@PathVariable int userId) {
        User user = this.userService.getById(userId);
        this.loginService.assertHasRightOn(user);
        List<User> contacts = this.userService.getContactsForUser(user);
        return contacts.stream()
                .map(this.userMapper::generate)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "{userId}/emails", method = RequestMethod.GET)
    @ResponseBody
    public List<EmailDTO> getEmails(@PathVariable int userId) {
        User user = this.userService.getById(userId);
        this.loginService.assertHasRightOn(user);
        List<Email> emails = this.emailService.getForUser(user);
        return emails.stream()
                .map(this.emailMapper::generate)
                .collect(Collectors.toList());
    }
}
