package com.vdurmont.vdmail.dto;

public class EmailDTO extends EntityDTO {
    private UserDTO sender;
    private UserDTO recipient;
    private String subject;
    private String content;

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

    @Override public String toString() {
        return "EmailDTO{" +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
