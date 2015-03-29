package com.vdurmont.vdmail;

import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.Entity;
import com.vdurmont.vdmail.model.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class AbstractTest {
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @After
    public void tearDownAbstractTest() {
        releaseTime();
    }

    protected static String randomString() {
        return UUID.randomUUID().toString();
    }

    protected static String randomEmail() {
        return randomString() + "@vdmail.vdurmont.com";
    }

    protected static Email generateValidEmail() {
        Email email = new Email();
        email.setSubject(randomString());
        email.setSender(generateUser());
        email.setRecipient(generateUser());
        email.setContent(randomString());
        return email;
    }

    protected static User generateUser() {
        User user = new User();
        user.setName(randomString());
        user.setAddress(randomEmail());
        return user;
    }

    protected static DateTime setCurrentDate(DateTime date) {
        DateTimeUtils.setCurrentMillisFixed(date.getMillis());
        return date;
    }

    protected static DateTime setCurrentDateNow() {
        DateTime now = DateTime.now();
        return setCurrentDate(now);
    }

    protected static void releaseTime() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    protected static <T extends Entity> void assertEntityEquals(T expected, T actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertEquals(expected.getId(), actual.getId());
        }
    }
}
