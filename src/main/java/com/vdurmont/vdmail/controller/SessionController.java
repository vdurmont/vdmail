package com.vdurmont.vdmail.controller;

import com.vdurmont.vdmail.dto.SessionDTO;
import com.vdurmont.vdmail.mapper.SessionMapper;
import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
@RequestMapping("sessions")
public class SessionController {
    @Inject private SessionMapper sessionMapper;
    @Inject private SessionService sessionService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public SessionDTO create(@RequestParam String email, @RequestParam String password) {
        Session session = this.sessionService.create(email, password);
        return this.sessionMapper.generate(session);
    }
}
