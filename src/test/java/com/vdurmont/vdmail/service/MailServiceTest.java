package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.model.User;
import org.junit.Test;

import javax.inject.Inject;

public class MailServiceTest extends AbstractSpringTest {
    @Inject private MailService mailService;

    private User user;

    @Test public void send_with_invalid_email_fails() {
        // GIVEN
        Email email = generateValidEmail();
        email.setToAddress(null);

        // THEN
        this.expectedException.expect(IllegalInputException.class);

        // WHEN
        this.mailService.send(this.user, email);
    }
}
