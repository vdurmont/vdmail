package com.vdurmont.vdmail.service.mailprovider;

import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;

public interface MailProvider {
    /**
     * Sends an email via the provider.
     *
     * @param email the email to send
     *
     * @throws UnavailableProviderException if the provider is not configured or unavailable at the moment.
     */
    void send(Email email) throws UnavailableProviderException;

    /**
     * Checks if a provider is enabled in the app configuration.
     *
     * @return false if disabled, true if enabled
     */
    boolean isEnabled();
}
