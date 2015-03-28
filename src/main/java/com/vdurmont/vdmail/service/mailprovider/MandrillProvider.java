package com.vdurmont.vdmail.service.mailprovider;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.User;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

import static com.vdurmont.vdmail.tools.Strings.isNullOrEmpty;

@Service
public class MandrillProvider implements MailProvider {
    private static final Logger LOGGER = Logger.getLogger(MandrillProvider.class);

    @Inject Environment env;
    private MandrillApi mandrillApi;

    @PostConstruct
    public void setUp() {
        String apiKey = this.env.getProperty("mandrill.api_key");
        if (isNullOrEmpty(apiKey)) {
            this.mandrillApi = new MandrillApi(apiKey);
        }
    }

    @Override public void send(User user, Email email) throws UnavailableProviderException {
        if (this.mandrillApi == null) {
            throw new UnavailableProviderException("Mandrill was not properly configured.");
        }
        MandrillMessage message = toMandrillMessage(user, email);
        try {
            this.mandrillApi.messages().send(message, true);
        } catch (MandrillApiError | IOException e) {
            LOGGER.warn("An error occurred while sending an email with Mandrill", e);
            throw new UnavailableProviderException(e);
        }
    }

    private static MandrillMessage toMandrillMessage(User user, Email email) {
        // TODO code me
        return null;
    }
}
