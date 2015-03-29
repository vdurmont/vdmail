package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class NoConnectedUserException extends VDMailException {
    public NoConnectedUserException() {
        super(HttpStatus.UNAUTHORIZED, "You have to be connected.");
    }
}
