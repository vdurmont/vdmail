package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.exception.NoConnectedUserException;
import com.vdurmont.vdmail.model.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public User getCurrentUser() {
        // TODO code me
        return null;
    }

    public User getRequiredCurrentUser() {
        User user = this.getCurrentUser();
        if (user == null) {
            throw new NoConnectedUserException();
        }
        return user;
    }
}
