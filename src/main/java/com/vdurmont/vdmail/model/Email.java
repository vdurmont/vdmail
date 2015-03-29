package com.vdurmont.vdmail.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@javax.persistence.Entity
@Table(name = "vd_email")
public class Email extends Entity {
    @ManyToOne
    @NotNull
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
    @NotEmpty
    @Column(name = "subject", nullable = false, length = 255)
    private String subject;
    @NotEmpty
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
//    private MailProviderType sentWith; TODO register the sender type

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
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
}
