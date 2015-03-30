package com.vdurmont.vdmail.controller;

import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.repository.EmailRepository;
import com.vdurmont.vdmail.repository.UserRepository;
import com.vdurmont.vdmail.service.mailprovider.MailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("admin")
// TODO protect every endpoint with admin rights (if it was a real project)
public class AdminController {
    @Inject private EmailRepository emailRepository;
    @Inject private UserRepository userRepository;

    @Inject private MandrillProvider mandrillProvider;
    @Inject private SendgridProvider sendgridProvider;

    @RequestMapping(value = "status", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("stats", this.generateStats());
        map.put("providers", this.generateProviders());
        return map;
    }

    private Map<String, Object> generateStats() {
        Map<String, Object> map = new HashMap<>();
        map.put("num_users", this.userRepository.count());
        map.put("num_active_users", this.userRepository.countByNameIsNotNull());
        map.put("num_emails_sent", this.emailRepository.count());
        return map;
    }

    private Map<String, Object> generateProviders() {
        Map<String, Object> map = new HashMap<>();
        map.put("mandrill", this.mandrillProvider.getStatus());
        map.put("sendgrid", this.sendgridProvider.getStatus());
        return map;
    }

    @RequestMapping(value = "providers/{provider}", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateProvider(@PathVariable String provider) {
        this.getProvider(provider).setActive(true);
    }

    @RequestMapping(value = "providers/{provider}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateProvider(@PathVariable String provider) {
        this.getProvider(provider).setActive(false);
    }

    private MailProvider getProvider(String providerString) {
        switch (providerString.trim().toLowerCase()) {
            case "mandrill":
                return this.mandrillProvider;
            case "sendgrid":
                return this.sendgridProvider;
            default:
                throw new IllegalInputException("Unknown provider: " + providerString);
        }
    }
}
