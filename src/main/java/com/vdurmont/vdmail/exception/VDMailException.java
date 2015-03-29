package com.vdurmont.vdmail.exception;

import com.vdurmont.vdmail.dto.HttpStatus;

public class VDMailException extends RuntimeException {
    private final HttpStatus status;

    public VDMailException(HttpStatus status, String msg) {
        super(msg);
        this.status = status;
    }

    public VDMailException(HttpStatus status, Throwable t) {
        super(t);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
