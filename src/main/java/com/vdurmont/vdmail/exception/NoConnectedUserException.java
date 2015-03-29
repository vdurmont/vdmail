package com.vdurmont.vdmail.exception;

public class NoConnectedUserException extends VDMailException {
    public NoConnectedUserException() {
        super("You have to be connected.");
    }
}
