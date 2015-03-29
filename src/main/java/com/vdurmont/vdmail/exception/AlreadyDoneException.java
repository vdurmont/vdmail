package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class AlreadyDoneException extends VDMailException {
    public AlreadyDoneException(String msg) {
        super(HttpStatus.CONFLICT, msg);
    }
}
