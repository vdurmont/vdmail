package com.vdurmont.vdmail.service.mailprovider;

import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;

public abstract class AbstractProvider implements MailProvider {
    private final String providerName;
    private boolean active;

    public AbstractProvider(String providerName) {
        this.providerName = providerName;
        this.active = true;
    }

    @Override public ProviderStatus getStatus() {
        if (!this.isConfigured()) {
            return ProviderStatus.NOT_CONFIGURED;
        } else {
            return this.active ? ProviderStatus.ACTIVE : ProviderStatus.INACTIVE;
        }
    }

    @Override public void setActive(boolean active) {
        this.active = active;
    }

    @Override public void send(Email email) throws UnavailableProviderException {
        if (this.getStatus() != ProviderStatus.ACTIVE) {
            throw new UnavailableProviderException(providerName + " is not available.");
        }
        this.doSend(email);
    }

    protected abstract void doSend(Email email) throws UnavailableProviderException;

    protected abstract boolean isConfigured();
}
