package com.vdurmont.vdmail.tools;

import com.vdurmont.vdmail.AbstractSimpleTest;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.model.Email;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmailsTest extends AbstractSimpleTest {
    @Test public void validate_with_full_email_returns_no_error() {
        Email email = generateValidEmail();
        assertEquals(0, Emails.validate(email).size());
    }

    @Test public void validate_without_senderName_returns_no_error() {
        Email email = generateValidEmail();
        email.getSender().setName(null);
        assertEquals(0, Emails.validate(email).size());
    }

    @Test public void validate_with_a_null_email_fails() {
        this.expectedException.expect(IllegalInputException.class);
        Emails.validate(null);
    }

    @Test public void validate_without_sender_returns_an_error() {
        Email email = generateValidEmail();
        email.setSender(null);
        assertTrue(Emails.validate(email).contains("sender"));
    }

    @Test public void validate_without_senderAddress_returns_an_error() {
        Email email = generateValidEmail();
        email.getSender().setAddress(null);
        assertTrue(Emails.validate(email).contains("sender_address"));
    }

    @Test public void validate_without_recipient_returns_an_error() {
        Email email = generateValidEmail();
        email.setRecipient(null);
        assertTrue(Emails.validate(email).contains("recipient"));
    }

    @Test public void validate_with_invalid_recipientAddress_returns_an_error() {
        Email email = generateValidEmail();

        email.getRecipient().setAddress("");
        assertTrue(Emails.validate(email).contains("recipient_address"));

        email.getRecipient().setAddress(randomString());
        assertTrue(Emails.validate(email).contains("recipient_address"));
    }

    @Test public void validate_without_subject_returns_an_error() {
        Email email = generateValidEmail();
        email.setSubject(null);
        assertTrue(Emails.validate(email).contains("subject"));
    }

    @Test public void validate_without_content_returns_an_error() {
        Email email = generateValidEmail();
        email.setContent(null);
        assertTrue(Emails.validate(email).contains("content"));
    }
}
