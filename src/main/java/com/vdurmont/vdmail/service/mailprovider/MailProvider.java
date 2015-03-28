package com.vdurmont.vdmail.service.mailprovider;

import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.UnavailableProviderException;

public interface MailProvider {
    /**
     * Sends an email via the provider.
     *
     * @param email the email to send
     *
     * @throws UnavailableProviderException if the provider is not configured or unavailable at the moment.
     */
    void send(Email email) throws UnavailableProviderException;
}
