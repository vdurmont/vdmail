package com.vdurmont.vdmail;

import com.vdurmont.vdmail.dto.Email;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.UUID;

public abstract class AbstractTest {
    @Rule public ExpectedException expectedException = ExpectedException.none();

    protected static String randomString() {
        return UUID.randomUUID().toString();
    }

    protected static String randomEmail() {
        return randomString() + "@vdmail.vdurmont.com";
    }

    protected static Email generateValidEmail() {
        Email email = new Email();
        email.setSubject(randomString());
        email.setToAddress(randomEmail());
        email.setToName(randomString());
        email.setContent(randomString());
        return email;
    }
}
