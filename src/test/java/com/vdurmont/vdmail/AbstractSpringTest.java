package com.vdurmont.vdmail;

import com.vdurmont.vdmail.config.AppConfig;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.EmailRepository;
import com.vdurmont.vdmail.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public abstract class AbstractSpringTest extends AbstractTest {
    @Inject private EmailRepository emailRepository;
    @Inject private UserRepository userRepository;

    @Before public void setUpSpringTest() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDownSpringTest() {
        this.emailRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    protected User createUser() {
        return this.userRepository.save(generateUser());
    }
}
