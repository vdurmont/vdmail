package com.vdurmont.vdmail.controller;

import com.vdurmont.vdmail.dto.SessionDTO;
import com.vdurmont.vdmail.mapper.SessionMapper;
import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.service.SessionService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("sessions")
public class SessionController {
    @Inject private SessionMapper sessionMapper;
    @Inject private SessionService sessionService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDTO create(HttpServletRequest request) {
        String email = null;
        String password = null;
        String header = request.getHeader("Authorization");
        try {
            header = header.replace("Basic ", "");
            String auth = StringUtils.newStringUtf8(Base64.decodeBase64(header));
            String[] tokens = auth.split(":");
            email = tokens[0];
            password = tokens[1];
        } catch (Exception ignored) {
        }

        Session session = this.sessionService.create(email, password);
        return this.sessionMapper.generate(session);
    }
}
