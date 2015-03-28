package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class MailServiceTest {
    @Inject private MailService mailService;

    @Test public void dummy_test() {
        assertNotNull(this.mailService);
    }
}
