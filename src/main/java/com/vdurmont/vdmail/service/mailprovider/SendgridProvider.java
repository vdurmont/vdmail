package com.vdurmont.vdmail.service.mailprovider;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.MailProviderType;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.vdurmont.vdmail.tools.Strings.isNullOrEmpty;

@Service
public class SendgridProvider extends AbstractProvider {
    private static final Logger LOGGER = Logger.getLogger(SendgridProvider.class);

    public static final String ENV_USERNAME = "sendgrid.username";
    public static final String ENV_PASSWORD = "sendgrid.password";

    @Inject Environment env;
    private SendGrid sendGrid;

    public SendgridProvider() {
        super(MailProviderType.SENDGRID);
    }

    @PostConstruct
    public void setUp() {
        String username = this.env.getProperty(ENV_USERNAME);
        String password = this.env.getProperty(ENV_PASSWORD);
        if (!isNullOrEmpty(username) && !isNullOrEmpty(password)) {
            this.sendGrid = new SendGrid(username, password);
        }
    }

    @Override public void doSend(Email email) throws UnavailableProviderException {
        SendGrid.Email sendgridEmail = toSendgridEmail(email);
        try {
            SendGrid.Response response = sendGrid.send(sendgridEmail);
            if (!response.getStatus()) {
                LOGGER.warn("An error occurred while sending an email with Sendgrid: " + response.getMessage());
                throw new UnavailableProviderException(response.getMessage());
            }
        } catch (SendGridException e) {
            LOGGER.warn("An error occurred while sending an email with Sendgrid", e);
            throw new UnavailableProviderException(e);
        }
    }

    protected static SendGrid.Email toSendgridEmail(Email email) {
        SendGrid.Email se = new SendGrid.Email();
        se.setFromName(email.getSender().getName());
        se.setFrom(email.getSender().getAddress());
        se.addTo(email.getRecipient().getAddress());
        se.setSubject(email.getSubject());
        se.setText(email.getContent());
        return se;
    }

    @Override protected boolean isConfigured() {
        return this.sendGrid != null;
    }
}
