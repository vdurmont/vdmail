package com.vdurmont.vdmail.model;

public enum MailProviderType implements EnumWithCode {
    CONSOLE(0), MANDRILL(1), SENDGRID(2);
    private final int code;

    private MailProviderType(int code) {
        this.code = code;
    }


    @Override public int getCode() {
        return this.code;
    }
}
