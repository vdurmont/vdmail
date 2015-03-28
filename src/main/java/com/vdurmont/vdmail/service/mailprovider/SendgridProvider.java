package com.vdurmont.vdmail.service.mailprovider;

import com.sendgrid.SendGrid;
import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.vdurmont.vdmail.tools.Strings.isNullOrEmpty;

@Service
public class SendgridProvider implements MailProvider {
    private SendGrid sendGrid;

    @Inject Environment env;

    @PostConstruct
    public void setUp() {
        String username = this.env.getProperty("sendgrid.username");
        String password = this.env.getProperty("sendgrid.password");
        if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
            this.sendGrid = new SendGrid(username, password);
        }
    }

    @Override public void send(Email email) throws UnavailableProviderException {
        if (this.sendGrid == null) {
            throw new UnavailableProviderException("Sendgrid was not properly configured.");
        }
        // TODO code me
    }
}
