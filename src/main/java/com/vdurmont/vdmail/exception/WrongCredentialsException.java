package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class WrongCredentialsException extends VDMailException {
    public WrongCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Wrong credentials");
    }
}
