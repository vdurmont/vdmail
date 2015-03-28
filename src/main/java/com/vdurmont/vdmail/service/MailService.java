package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.service.mailprovider.ConsoleMailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class MailService {
    @Inject private ConsoleMailProvider consoleMailProvider;
    @Inject private MandrillProvider mandrillProvider;
    @Inject private SendgridProvider sendgridProvider;

    public void send(Email email) {
        // TODO code me
    }
}
