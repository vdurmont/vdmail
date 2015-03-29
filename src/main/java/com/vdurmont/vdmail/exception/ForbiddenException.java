package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class ForbiddenException extends VDMailException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "You are not allowed to perform this operation.");
    }
}
