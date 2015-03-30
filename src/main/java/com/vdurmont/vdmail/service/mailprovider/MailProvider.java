package com.vdurmont.vdmail.service.mailprovider;

import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.MailProviderType;

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
    ProviderStatus getStatus();

    /**
     * Activates/deactivates a provider
     */
    void setActive(boolean active);

    /**
     * Returns the type of the provider
     */
    MailProviderType getType();

    public static enum ProviderStatus {
        /**
         * The provider is active
         */
        ACTIVE,

        /**
         * The provider is inactive (has been deactivated by an admin)
         */
        INACTIVE,

        /**
         * The provider was not configured
         */
        NOT_CONFIGURED
    }
}
