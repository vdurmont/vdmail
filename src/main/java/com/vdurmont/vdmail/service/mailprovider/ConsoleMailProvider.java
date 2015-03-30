package com.vdurmont.vdmail.service.mailprovider;

import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConsoleMailProvider implements MailProvider {
    private static final Logger LOGGER = Logger.getLogger(ConsoleMailProvider.class);

    @Override public void send(Email email) throws UnavailableProviderException {
        LOGGER.debug("ConsoleMailService sending email=" + email);
    }

    @Override public ProviderStatus getStatus() {
        return ProviderStatus.ACTIVE;
    }

    @Override public void setActive(boolean active) {
        // No-op
    }
}
