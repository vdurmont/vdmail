package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.service.mailprovider.ConsoleMailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.inject.Inject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailServiceTest extends AbstractSpringTest {
    @InjectMocks @Inject private EmailService emailService;

    @Mock private ConsoleMailProvider consoleMailProvider;
    @Mock private MandrillProvider mandrillProvider;
    @Mock private SendgridProvider sendgridProvider;

    @Test public void send_with_invalid_sender_fails() {
        // GIVEN

        // THEN
        this.expectedException.expect(IllegalInputException.class);

        // WHEN
        this.emailService.send(null, this.createUser(), randomString(), randomString());
    }

    @Test public void if_no_provider_is_available_we_use_the_ConsoleMailProvider() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.isEnabled()).thenReturn(false);
        when(this.sendgridProvider.isEnabled()).thenReturn(false);
        this.emailService.setUp();

        // WHEN
        this.emailService.send(this.createUser(), this.createUser(), randomString(), randomString());

        // THEN
        verify(this.consoleMailProvider).send(any(Email.class));
    }

    @Test public void if_a_provider_is_available_we_use_it() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.isEnabled()).thenReturn(true);
        when(this.sendgridProvider.isEnabled()).thenReturn(false);
        this.emailService.setUp();

        // WHEN
        this.emailService.send(this.createUser(), this.createUser(), randomString(), randomString());

        // THEN
        verify(this.mandrillProvider).send(any(Email.class));
        verify(this.consoleMailProvider, never()).send(any(Email.class));
    }

    @Test public void if_mandrill_is_unavailable_we_fallback_on_sendgrid() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.isEnabled()).thenReturn(true);
        doThrow(UnavailableProviderException.class).when(this.mandrillProvider).send(any(Email.class));
        when(this.sendgridProvider.isEnabled()).thenReturn(true);
        this.emailService.setUp();

        // WHEN
        this.emailService.send(this.createUser(), this.createUser(), randomString(), randomString());

        // THEN
        verify(this.mandrillProvider).send(any(Email.class));
        verify(this.sendgridProvider).send(any(Email.class));
        verify(this.consoleMailProvider, never()).send(any(Email.class));
    }
}
