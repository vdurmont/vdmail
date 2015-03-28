package com.vdurmont.vdmail.exception;

public class VDMailException extends RuntimeException {
    public VDMailException(String msg) {
        super(msg);
    }

    public VDMailException(Throwable t) {
        super(t);
    }
}
