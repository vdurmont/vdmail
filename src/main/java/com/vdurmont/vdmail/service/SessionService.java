package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.exception.NotFoundException;
import com.vdurmont.vdmail.exception.SessionExpiredException;
import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.repository.SessionRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SessionService {
    @Inject private SessionRepository sessionRepository;

    public Session getByToken(String token) {
        Session session = this.sessionRepository.findByToken(token);
        if (session == null) {
            throw new NotFoundException("No session with token '" + token + "'");
        } else if (session.getExpirationDate().isBefore(DateTime.now())) {
            throw new SessionExpiredException();
        }
        return session;
    }
}
