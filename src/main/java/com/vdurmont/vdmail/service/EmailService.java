package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.dto.HttpStatus;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.exception.VDMailException;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.EmailRepository;
import com.vdurmont.vdmail.service.mailprovider.ConsoleMailProvider;
import com.vdurmont.vdmail.service.mailprovider.MailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import com.vdurmont.vdmail.tools.Emails;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    private static final Duration DELAY = Duration.standardMinutes(1);

    @Inject private EmailRepository emailRepository;

    @Inject private ConsoleMailProvider consoleMailProvider;
    @Inject private MandrillProvider mandrillProvider;
    @Inject private SendgridProvider sendgridProvider;

    private List<MailProvider> providers;
    private int providerIndex;
    private DateTime nextReset;

    /**
     * The setup populates the list of available providers according to the configuration of the app.
     */
    @PostConstruct
    public void setUp() {
        this.providers = new ArrayList<>();
        this.providerIndex = 0;

        if (this.mandrillProvider.isEnabled()) {
            this.providers.add(this.mandrillProvider);
        }

        if (this.sendgridProvider.isEnabled()) {
            this.providers.add(this.sendgridProvider);
        }

        if (this.providers.size() == 0) {
            this.providers.add(this.consoleMailProvider);
        }
    }

    /**
     * Sends an email.
     *
     * @param sender    the sender
     * @param recipient the recipient
     * @param subject   the subject of the mail
     * @param content   the content of the mail
     */
    public Email send(User sender, User recipient, String subject, String content) {
        // Build the email
        Email email = new Email();
        email.setSender(sender);
        email.setRecipient(recipient);
        email.setSubject(subject);
        email.setContent(content);

        // Check that it's valid
        List<String> errors = Emails.validate(email);
        if (errors.size() > 0) {
            throw new IllegalInputException("Invalid email (invalid fields: " + errors + ")");
        }

        // Reset the providers if needed
        if (this.nextReset != null && DateTime.now().isAfter(this.nextReset)) {
            this.nextReset = null;
            this.providerIndex = 0;
        }

        this.doSendEmail(email);

        return this.emailRepository.save(email);
    }

    private void doSendEmail(Email email) {
        boolean sent = false;
        while (!sent && this.providerIndex < this.providers.size()) {
            try {
                MailProvider provider = this.providers.get(this.providerIndex);
                provider.send(email);
                sent = true;
            } catch (UnavailableProviderException e) {
                // If the provider failed to send the message, we skip it for the next few minutes
                // TODO implement a real throttling where the delay increases at each failure
                // TODO set the reset date for each provider
                this.providerIndex++;
                this.nextReset = DateTime.now().plus(DELAY);
            }
        }

        if (!sent) {
            // We didn't find any available provider
            // TODO maybe we could try to start again at the begining of the list?
            throw new VDMailException(HttpStatus.INTERNAL_SERVER_ERROR, "No mail provider available.");
        }
    }
}
