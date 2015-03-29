package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.exception.AlreadyDoneException;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.exception.NotFoundException;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.UserRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserServiceTest extends AbstractSpringTest {
    @Inject private UserRepository userRepository;
    @Inject private UserService userService;
    @Inject private PasswordEncoder passwordEncoder;

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

    @Test public void getContactsForUser_if_no_email_sent_returns_empty_list() {
        // GIVEN
        User user = this.createUser();

        // WHEN
        List<User> contacts = this.userService.getContactsForUser(user);

        // THEN
        assertEquals(0, contacts.size());
    }

    @Test public void getById_with_unknown_id_fails() {
        // GIVEN

        // THEN
        this.expectedException.expect(NotFoundException.class);

        // WHEN
        this.userService.getById(Integer.MAX_VALUE);
    }

    @Test public void getById_returns_the_user() {
        // GIVEN
        User expected = this.createUser();

        // WHEN
        User actual = this.userService.getById(expected.getId());

        // THEN
        assertEntityEquals(expected, actual);
    }

    @Test public void getContactsForUser_returns_the_recipients_of_this_user_ordered_by_last_email() {
        // GIVEN
        User user = this.createUser();

        User notContact = this.createUser();
        User contact1 = this.createUser();
        User contact2 = this.createUser();
        User contact3 = this.createUser();

        DateTime start = DateTime.now().minusDays(3);

        setCurrentDate(start);
        this.createEmail(user, contact1);

        setCurrentDate(start.plusHours(1));
        this.createEmail(notContact, user); // Should not influence the request

        setCurrentDate(start.plusHours(2));
        this.createEmail(user, contact3);

        setCurrentDate(start.plusHours(3));
        this.createEmail(user, contact2);

        setCurrentDate(start.plusHours(4));
        this.createEmail(user, contact2);

        setCurrentDate(start.plusHours(5));
        this.createEmail(contact3, user); // Should not influence the request

        // WHEN
        List<User> contacts = this.userService.getContactsForUser(user);

        // THEN
        assertEquals(3, contacts.size());
        assertEntityEquals(contact2, contacts.get(0));
        assertEntityEquals(contact3, contacts.get(1));
        assertEntityEquals(contact1, contacts.get(2));
    }

    @Test public void create_with_a_user_without_name_updates_the_user() {
        // GIVEN
        String name = randomString();
        String address = randomEmail();
        String pwd = randomString();
        User expected = new User();
        expected.setAddress(address);
        expected = this.userRepository.save(expected);

        // WHEN
        User actual = this.userService.create(name, address, pwd);

        // THEN
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(name, actual.getName());
        assertTrue(this.passwordEncoder.matches(pwd, actual.getPassword()));
    }

    @Test public void create_with_a_user_with_a_name_fails() {
        // GIVEN
        User user = this.createUser();

        // THEN
        this.expectedException.expect(AlreadyDoneException.class);

        // WHEN
        this.userService.create(randomString(), user.getAddress(), randomString());
    }

    @Test public void create_with_an_unknown_address_creates_the_user() {
        // GIVEN
        String name = randomString();
        String address = randomEmail();
        String pwd = randomString();

        // WHEN
        User user = this.userService.create(name, address, pwd);

        // THEN
        assertNotNull(user.getId());
        assertEquals(address, user.getAddress());
        assertEquals(name, user.getName());
        assertTrue(this.passwordEncoder.matches(pwd, user.getPassword()));
    }

    @Test public void create_with_bad_arguments_fails() {
        this.createAndAssertIllegalInput(null, randomEmail(), randomString());
        this.createAndAssertIllegalInput("", randomEmail(), randomString());
        this.createAndAssertIllegalInput(randomString(), null, randomString());
        this.createAndAssertIllegalInput(randomString(), "", randomString());
        this.createAndAssertIllegalInput(randomString(), randomString(), randomString());
        this.createAndAssertIllegalInput(randomString(), randomEmail(), null);
        this.createAndAssertIllegalInput(randomString(), randomEmail(), "");
    }

    private void createAndAssertIllegalInput(String name, String address, String password) {
        try {
            this.userService.create(name, address, password);
            fail("Failure with name=" + name + ", address=" + address + ", password=" + password);
        } catch (IllegalInputException ignored) {
        }
    }
}
