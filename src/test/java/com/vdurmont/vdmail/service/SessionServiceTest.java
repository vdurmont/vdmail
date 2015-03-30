package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.exception.WrongCredentialsException;
import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.SessionRepository;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SessionServiceTest extends AbstractSpringTest {
    @Inject private SessionRepository sessionRepository;
    @Inject private SessionService sessionService;

    @Test public void create_with_unknown_address_fails() {
        // GIVEN

        // THEN
        this.expectedException.expect(WrongCredentialsException.class);

        // WHEN
        this.sessionService.create(randomEmail(), randomString());
    }

    @Test public void create_with_wrong_password_fails() {
        // GIVEN
        User user = this.createUser();

        // THEN
        this.expectedException.expect(WrongCredentialsException.class);

        // WHEN
        this.sessionService.create(user.getAddress(), randomString());
    }

    @Test public void create_creates_a_session() {
        // GIVEN
        String pwd = randomString();
        User user = this.createUser(pwd);
        DateTime now = setCurrentDateNow();

        // WHEN
        Session session = this.sessionService.create(user.getAddress(), pwd);

        // THEN
        assertNotNull(session.getId());
        assertEntityEquals(user, session.getUser());
        assertEquals(now.plusHours(1), session.getExpirationDate());
        assertNotNull(session.getToken());
    }

    @Test public void create_deletes_the_old_sessions() {
        // GIVEN
        String pwd = randomString();
        User user = this.createUser(pwd);
        this.sessionService.create(user.getAddress(), pwd);
        this.sessionService.create(user.getAddress(), pwd);
        this.sessionService.create(user.getAddress(), pwd);

        // WHEN
        Session session = this.sessionService.create(user.getAddress(), pwd);

        // THEN
        List<Session> sessions = this.sessionRepository.findAllByUser(user);
        assertEquals(1, sessions.size());
        assertEntityEquals(session, sessions.get(0));
    }
}
