package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.service.mailprovider.ConsoleMailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MailServiceTest extends AbstractSpringTest {
    @InjectMocks private MailService mailService;

    @Mock private ConsoleMailProvider consoleMailProvider;
    @Mock private MandrillProvider mandrillProvider;
    @Mock private SendgridProvider sendgridProvider;

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

    @Test public void if_no_provider_is_available_we_use_the_ConsoleMailProvider() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.isEnabled()).thenReturn(false);
        when(this.sendgridProvider.isEnabled()).thenReturn(false);
        this.mailService.setUp(); // Manually called because we use Mockito

        Email email = generateValidEmail();

        // WHEN
        this.mailService.send(user, email);

        // THEN
        verify(this.consoleMailProvider).send(user, email);
    }

    @Test public void if_a_provider_is_available_we_use_it() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.isEnabled()).thenReturn(true);
        when(this.sendgridProvider.isEnabled()).thenReturn(false);
        this.mailService.setUp(); // Manually called because we use Mockito

        Email email = generateValidEmail();

        // WHEN
        this.mailService.send(user, email);

        // THEN
        verify(this.mandrillProvider).send(user, email);
        verify(this.consoleMailProvider, never()).send(user, email);
    }

    @Test public void if_mandrill_is_unavailable_we_fallback_on_sendgrid() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.isEnabled()).thenReturn(true);
        doThrow(UnavailableProviderException.class).when(this.mandrillProvider).send(any(User.class), any(Email.class));
        when(this.sendgridProvider.isEnabled()).thenReturn(true);
        this.mailService.setUp(); // Manually called because we use Mockito

        Email email = generateValidEmail();

        // WHEN
        this.mailService.send(user, email);

        // THEN
        verify(this.mandrillProvider).send(user, email);
        verify(this.sendgridProvider).send(user, email);
        verify(this.consoleMailProvider, never()).send(user, email);
    }
}
