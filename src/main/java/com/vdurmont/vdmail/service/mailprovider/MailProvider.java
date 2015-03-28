package com.vdurmont.vdmail.service.mailprovider;

import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.User;

public interface MailProvider {
    /**
     * Sends an email via the provider.
     *
     * @param user  the sender
     * @param email the email to send
     *
     * @throws UnavailableProviderException if the provider is not configured or unavailable at the moment.
     */
    void send(User user, Email email) throws UnavailableProviderException;
}
