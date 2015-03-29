package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.dto.HttpStatus;
import com.vdurmont.vdmail.exception.NotFoundException;
import com.vdurmont.vdmail.exception.SessionExpiredException;
import com.vdurmont.vdmail.exception.VDMailException;
import com.vdurmont.vdmail.exception.WrongCredentialsException;
import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.SessionRepository;
import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {
    private static final int MAX_TOKEN_ATTEMPTS = 3;

    @Inject private PasswordEncoder passwordEncoder;
    @Inject private SessionRepository sessionRepository;
    @Inject private UserService userService;

    public Session getByToken(String token) {
        Session session = this.sessionRepository.findByToken(token);
        if (session == null) {
            throw new NotFoundException("No session with token '" + token + "'");
        } else if (session.getExpirationDate().isBefore(DateTime.now())) {
            throw new SessionExpiredException();
        }
        return session;
    }

    public Session create(String address, String password) {
        User user;
        try {
            user = this.userService.getByAddress(address);
        } catch (NotFoundException e) {
            throw new WrongCredentialsException();
        }

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongCredentialsException();
        }

        List<Session> previousSessions = this.sessionRepository.findAllByUser(user);

        Session session = new Session();
        session.setUser(user);
        session.setExpirationDate(DateTime.now().plusHours(1));

        int tokenAttempts = 0;
        while (session.getId() == null) {
            try {
                session.setToken(UUID.randomUUID().toString());
                session = this.sessionRepository.save(session);
            } catch (Exception e) {
                if (tokenAttempts == MAX_TOKEN_ATTEMPTS) {
                    throw new VDMailException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to generate session token");
                }
                tokenAttempts++;
            }
        }

        this.sessionRepository.deleteInBatch(previousSessions);

        return session;
    }
}
