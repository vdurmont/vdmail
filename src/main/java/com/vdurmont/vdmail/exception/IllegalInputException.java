package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class IllegalInputException extends VDMailException {
    public IllegalInputException(String msg) {
        super(HttpStatus.BAD_REQUEST, msg);
    }
}
