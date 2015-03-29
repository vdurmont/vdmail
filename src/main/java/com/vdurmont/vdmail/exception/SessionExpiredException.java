package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class SessionExpiredException extends VDMailException {
    public SessionExpiredException() {
        super(HttpStatus.AUTHENTICATION_TIMEOUT, "Your session is expired.");
    }
}
