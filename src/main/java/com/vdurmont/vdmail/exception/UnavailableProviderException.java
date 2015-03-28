package com.vdurmont.vdmail.exception;

public class UnavailableProviderException extends Exception {
    public UnavailableProviderException(Throwable e) {
        super(e);
    }

    public UnavailableProviderException(String msg) {
        super(msg);
    }
}
