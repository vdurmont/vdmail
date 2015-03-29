package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class NotFoundException extends VDMailException {
    public NotFoundException(String msg) {
        super(HttpStatus.NOT_FOUND, msg);
    }
}
