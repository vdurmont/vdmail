package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.exception.ForbiddenException;
import com.vdurmont.vdmail.exception.NoConnectedUserException;
import com.vdurmont.vdmail.filter.TokenFilter;
import com.vdurmont.vdmail.model.User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        TokenFilter.VDMailAuthentication auth = (TokenFilter.VDMailAuthentication) securityContext.getAuthentication();
        if (auth == null || auth.getPrincipal() instanceof String) {
            return null;
        } else {
            return (User) auth.getPrincipal();
        }
    }

    public User getRequiredCurrentUser() {
        User user = this.getCurrentUser();
        if (user == null) {
            throw new NoConnectedUserException();
        }
        return user;
    }

    public void assertHasRightOn(User user) {
        User current = this.getRequiredCurrentUser();
        if (!current.getId().equals(user.getId())) {
            throw new ForbiddenException();
        }
    }
}
