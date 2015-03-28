package com.vdurmont.vdmail.tools;

import com.vdurmont.vdmail.AbstractSimpleTest;
import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.IllegalInputException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmailsTest extends AbstractSimpleTest {
    @Test public void validate_with_full_email_returns_no_error() {
        Email email = generateValidEmail();
        assertEquals(0, Emails.validate(email).size());
    }

    @Test public void validate_without_toName_returns_no_error() {
        Email email = generateValidEmail();
        email.setToName(null);
        assertEquals(0, Emails.validate(email).size());
    }

    @Test public void validate_with_a_null_email_fails() {
        this.expectedException.expect(IllegalInputException.class);
        Emails.validate(null);
    }

    @Test public void validate_without_toAddress_returns_an_error() {
        Email email = generateValidEmail();
        email.setToAddress(null);
        assertTrue(Emails.validate(email).contains("toAddress"));
    }

    @Test public void validate_with_invalid_toAddress_returns_an_error() {
        Email email = generateValidEmail();

        email.setToAddress("");
        assertTrue(Emails.validate(email).contains("toAddress"));

        email.setToAddress(randomString());
        assertTrue(Emails.validate(email).contains("toAddress"));
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
