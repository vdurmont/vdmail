package com.vdurmont.vdmail.dto;

public enum HttpStatus {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    AUTHENTICATION_TIMEOUT(419),
    INTERNAL_SERVER_ERROR(500);

    public final int code;

    private HttpStatus(int code) {
        this.code = code;
    }
}
