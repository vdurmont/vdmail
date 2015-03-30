package com.vdurmont.vdmail.dto;

import com.vdurmont.vdmail.model.MailProviderType;

public class EmailDTO extends EntityDTO {
    private UserDTO sender;
    private UserDTO recipient;
    private String subject;
    private String content;
    private MailProviderType provider;

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(UserDTO recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MailProviderType getProvider() {
        return provider;
    }

    public void setProvider(MailProviderType provider) {
        this.provider = provider;
    }

    @Override public String toString() {
        return "EmailDTO{" +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", provider=" + provider +
                '}';
    }
}
