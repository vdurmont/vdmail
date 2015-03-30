package com.vdurmont.vdmail.service.mailprovider;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.User;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

import static com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import static com.vdurmont.vdmail.tools.Strings.isNullOrEmpty;

@Service
public class MandrillProvider extends AbstractProvider {
    private static final Logger LOGGER = Logger.getLogger(MandrillProvider.class);
    public static final String ENV_API_KEY = "mandrill.api_key";

    @Inject Environment env;
    private MandrillApi mandrillApi;

    public MandrillProvider() {
        super("Mandrill");
    }

    @PostConstruct
    public void setUp() {
        String apiKey = this.env.getProperty(ENV_API_KEY);
        if (!isNullOrEmpty(apiKey)) {
            this.mandrillApi = new MandrillApi(apiKey);
        }
    }

    @Override protected void doSend(Email email) throws UnavailableProviderException {
        MandrillMessage message = toMandrillMessage(email);
        try {
            this.mandrillApi.messages().send(message, true);
        } catch (MandrillApiError | IOException e) {
            LOGGER.warn("An error occurred while sending an email with Mandrill", e);
            throw new UnavailableProviderException(e);
        }
    }

    protected static MandrillMessage toMandrillMessage(Email email) {
        MandrillMessage message = new MandrillMessage();
        message.setFromName(email.getSender().getName());
        message.setFromEmail(email.getSender().getAddress());
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<>();
        recipients.add(createMandrillRecipient(email.getRecipient(), Recipient.Type.TO));
        message.setTo(recipients);
        message.setSubject(email.getSubject());
        message.setText(email.getContent());
        return message;
    }

    private static Recipient createMandrillRecipient(User user, Recipient.Type type) {
        Recipient recipient = new Recipient();
        recipient.setName(user.getName());
        recipient.setEmail(user.getAddress());
        recipient.setType(type);
        return recipient;
    }

    @Override protected boolean isConfigured() {
        return this.mandrillApi != null;
    }
}
