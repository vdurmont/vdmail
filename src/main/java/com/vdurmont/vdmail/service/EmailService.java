package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.dto.HttpStatus;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.exception.UnavailableProviderException;
import com.vdurmont.vdmail.exception.VDMailException;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.MailProviderType;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.EmailRepository;
import com.vdurmont.vdmail.service.mailprovider.ConsoleMailProvider;
import com.vdurmont.vdmail.service.mailprovider.MailProvider;
import com.vdurmont.vdmail.service.mailprovider.MandrillProvider;
import com.vdurmont.vdmail.service.mailprovider.SendgridProvider;
import com.vdurmont.vdmail.tools.Emails;
import org.joda.time.Duration;
import org.springframework.data.domain.Sort;
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

    /**
     * The setup populates the list of available providers according to the configuration of the app.
     */
    @PostConstruct
    public void setUp() {
        this.providers = new ArrayList<>();

        if (this.mandrillProvider.getStatus() != MailProvider.ProviderStatus.NOT_CONFIGURED) {
            this.providers.add(this.mandrillProvider);
        }

        if (this.sendgridProvider.getStatus() != MailProvider.ProviderStatus.NOT_CONFIGURED) {
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
        Email email = this.generateEmail(sender, recipient, subject, content);

        // Check that it's valid
        List<String> errors = Emails.validate(email);
        if (errors.size() > 0) {
            throw new IllegalInputException("Invalid email (invalid fields: " + errors + ")");
        }

        this.doSendEmail(email);

        return this.emailRepository.save(email);
    }

    private void doSendEmail(Email email) {
        boolean sent = false;
        int providerIndex = 0;
        while (!sent && providerIndex < this.providers.size()) {
            try {
                MailProvider provider = this.providers.get(providerIndex);
                provider.send(email);
                sent = true;
                email.setProvider(provider.getType());
            } catch (UnavailableProviderException e) {
                // TODO implement a throttling to skip the provider for a few minutes if it failed
                providerIndex++;
            }
        }

        if (!sent) {
            // We didn't find any available provider
            throw new VDMailException(HttpStatus.INTERNAL_SERVER_ERROR, "No mail provider available.");
        }
    }

    private Email generateEmail(User sender, User recipient, String subject, String content) {
        Email email = new Email();
        email.setSender(sender);
        email.setRecipient(recipient);
        email.setSubject(subject);
        email.setContent(content);
        return email;
    }

    public Email create(User sender, User recipient, String subject, String content, MailProviderType provider) {
        Email email = this.generateEmail(sender, recipient, subject, content);
        email.setProvider(provider);
        return this.emailRepository.save(email);
    }

    public List<Email> getForUser(User user) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return this.emailRepository.findAllBySender(user, sort);
    }
}
