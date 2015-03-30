package com.vdurmont.vdmail;

import com.vdurmont.vdmail.config.AppConfig;
import com.vdurmont.vdmail.config.SecurityConfig;
import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.MailProviderType;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.EmailRepository;
import com.vdurmont.vdmail.repository.SessionRepository;
import com.vdurmont.vdmail.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, SecurityConfig.class})
public abstract class AbstractSpringTest extends AbstractTest {
    @Inject private EmailRepository emailRepository;
    @Inject private PasswordEncoder passwordEncoder;
    @Inject private SessionRepository sessionRepository;
    @Inject private UserRepository userRepository;

    @Before public void setUpSpringTest() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDownSpringTest() {
        this.emailRepository.deleteAllInBatch();
        this.sessionRepository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();
    }

    protected User createUser() {
        return this.createUser(randomString());
    }

    protected User createUser(String password) {
        User user = generateUser();
        user.setPassword(this.passwordEncoder.encode(password));
        return this.userRepository.save(user);
    }

    protected Email createEmail(User sender, User recipient) {
        Email email = new Email();
        email.setSender(sender);
        email.setRecipient(recipient);
        email.setSubject(randomString());
        email.setContent(randomString());
        email.setProvider(randomEnum(MailProviderType.class));
        return this.emailRepository.save(email);
    }
}
