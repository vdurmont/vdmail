package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.MailProviderType;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.service.mailprovider.ConsoleMailProvider;
import com.vdurmont.vdmail.service.mailprovider.MailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    @Before
    public void setUp() {
        when(this.consoleMailProvider.getType()).thenReturn(MailProviderType.CONSOLE);
        when(this.mandrillProvider.getType()).thenReturn(MailProviderType.MANDRILL);
        when(this.sendgridProvider.getType()).thenReturn(MailProviderType.SENDGRID);
    }

    @Test public void send_with_invalid_sender_fails() {
        // GIVEN

        // THEN
        this.expectedException.expect(IllegalInputException.class);

        // WHEN
        this.emailService.send(null, this.createUser(), randomString(), randomString());
    }

    @Test public void if_no_provider_is_available_we_use_the_ConsoleMailProvider() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.getStatus()).thenReturn(MailProvider.ProviderStatus.INACTIVE);
        when(this.sendgridProvider.getStatus()).thenReturn(MailProvider.ProviderStatus.INACTIVE);
        this.emailService.setUp();

        // WHEN
        this.emailService.send(this.createUser(), this.createUser(), randomString(), randomString());

        // THEN
        verify(this.consoleMailProvider).send(any(Email.class));
    }

    @Test public void if_a_provider_is_available_we_use_it() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.getStatus()).thenReturn(MailProvider.ProviderStatus.ACTIVE);
        when(this.sendgridProvider.getStatus()).thenReturn(MailProvider.ProviderStatus.INACTIVE);
        this.emailService.setUp();

        // WHEN
        this.emailService.send(this.createUser(), this.createUser(), randomString(), randomString());

        // THEN
        verify(this.mandrillProvider).send(any(Email.class));
        verify(this.consoleMailProvider, never()).send(any(Email.class));
    }

    @Test public void if_mandrill_is_unavailable_we_fallback_on_sendgrid() throws UnavailableProviderException {
        // GIVEN
        when(this.mandrillProvider.getStatus()).thenReturn(MailProvider.ProviderStatus.ACTIVE);
        doThrow(UnavailableProviderException.class).when(this.mandrillProvider).send(any(Email.class));
        when(this.sendgridProvider.getStatus()).thenReturn(MailProvider.ProviderStatus.ACTIVE);
        this.emailService.setUp();

        // WHEN
        this.emailService.send(this.createUser(), this.createUser(), randomString(), randomString());

        // THEN
        verify(this.mandrillProvider).send(any(Email.class));
        verify(this.sendgridProvider).send(any(Email.class));
        verify(this.consoleMailProvider, never()).send(any(Email.class));
    }

    @Test public void send_creates_an_email() {
        // GIVEN
        User sender = this.createUser();
        User recipient = this.createUser();
        String subject = randomString();
        String content = randomString();

        DateTime now = setCurrentDateNow();

        // WHEN
        Email email = this.emailService.send(sender, recipient, subject, content);

        // THEN
        assertNotNull(email.getId());
        assertEquals(now, email.getCreatedDate());
        assertEquals(subject, email.getSubject());
        assertEquals(content, email.getContent());
        assertEntityEquals(sender, email.getSender());
        assertEntityEquals(recipient, email.getRecipient());
        assertEquals(MailProviderType.CONSOLE, email.getProvider());
    }

    @Test public void getForUser_returns_the_emails_sent_by_this_user() {
        // GIVEN
        User user = this.createUser();
        User other1 = this.createUser();
        User other2 = this.createUser();
        User other3 = this.createUser();

        DateTime start = DateTime.now().minusDays(1);

        setCurrentDate(start);
        Email email1 = this.emailService.send(user, other1, randomString(), randomString());

        setCurrentDate(start.plusHours(1));
        this.emailService.send(other1, user, randomString(), randomString());

        setCurrentDate(start.plusHours(2));
        Email email2 = this.emailService.send(user, other2, randomString(), randomString());

        setCurrentDate(start.plusHours(3));
        Email email3 = this.emailService.send(user, other2, randomString(), randomString());

        setCurrentDate(start.plusHours(4));
        this.emailService.send(other3, other2, randomString(), randomString());

        // WHEN
        List<Email> emails = this.emailService.getForUser(user);

        // THEN
        assertEquals(3, emails.size());
        assertEntityEquals(email3, emails.get(0));
        assertEntityEquals(email2, emails.get(1));
        assertEntityEquals(email1, emails.get(2));
    }
}
