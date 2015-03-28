package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.service.mailprovider.ConsoleMailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import com.vdurmont.vdmail.tools.Emails;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class MailService {
    @Inject private ConsoleMailProvider consoleMailProvider;
    @Inject private MandrillProvider mandrillProvider;
    @Inject private SendgridProvider sendgridProvider;

    public void send(User user, Email email) {
        List<String> errors = Emails.validate(email);
        if (errors.size() > 0) {
            throw new IllegalInputException("Invalid email (invalid fields: " + errors + ")");
        }
        // TODO code me
    }
}
