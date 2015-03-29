package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.model.User;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserServiceTest extends AbstractSpringTest {
    @Inject private UserService userService;

    @Test public void getOrCreate_with_existing_address() {
        // GIVEN
        User expected = this.createUser();

        // WHEN
        User actual = this.userService.getOrCreate(expected.getAddress());

        // THEN
        assertEntityEquals(expected, actual);
    }

    @Test public void getOrCreate_with_unknown_address() {
        // GIVEN
        String address = randomEmail();
        DateTime now = setCurrentDateNow();

        // WHEN
        User user = this.userService.getOrCreate(address);

        // THEN
        assertNotNull(user.getId());
        assertEquals(now, user.getCreatedDate());
        assertNull(user.getName());
        assertEquals(address, user.getAddress());
    }
}
