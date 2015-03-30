package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.model.MailProviderType;
import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.SessionRepository;
import org.joda.time.DateTime;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Service
public class PopulateService {
    private static final String ENV_ENABLED = "vdmail.populate_enabled";

    @Inject private Environment env;
    @Inject private EmailService emailService;
    @Inject private SessionRepository sessionRepository;
    @Inject private UserService userService;

    private boolean enabled;

    @PostConstruct
    public void setUp() {
        String enabled = this.env.getProperty(ENV_ENABLED, "true");
        try {
            this.enabled = Boolean.valueOf(enabled);
        } catch (IllegalArgumentException e) {
            this.enabled = true;
        }
    }

    public void populate() {
        if (this.enabled) {
            // Create some users
            User superman = this.userService.create("Clark Kent", "superman@vdmail.vdurmont.com", "superman");
            User batman = this.userService.create("Bruce Wayne", "batman@vdmail.vdurmont.com", "batman");
            User spiderman = this.userService.create("Peter Parker", "spiderman@vdmail.vdurmont.com", "spiderman");

            // Send some emails
            this.emailService.create(superman, batman, "Hi from Gotham City", "Hey dude! How's it going? Cheers.", MailProviderType.MANDRILL);
            this.emailService.create(batman, superman, "Re: Hi from Gotham City", "Everything's all right. Catching bad guys.", MailProviderType.MANDRILL);
            this.emailService.create(spiderman, batman, "Hi from NYC", "Hey! I have a huge villain who looks like an octopus. Little help please!", MailProviderType.SENDGRID);
            this.emailService.create(batman, spiderman, "Re: Hi from NYC", "Sorry, I have my hands full with some kind of clown right know :(", MailProviderType.SENDGRID);

            // Init a session
            Session session = new Session();
            session.setUser(batman);
            session.setToken("LET_ME_IN");
            session.setExpirationDate(DateTime.now().plusDays(3));
            this.sessionRepository.save(session);
        }
    }
}
