package com.vdurmont.vdmail.service.mailprovider;

import com.vdurmont.vdmail.AbstractSpringTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class SendgridProviderTest extends AbstractSpringTest {
    @InjectMocks private SendgridProvider sendgridProvider;

    @Mock private Environment env;

    @Test public void setUp_tests() {
        // Null username
        when(this.env.getProperty(SendgridProvider.ENV_USERNAME)).thenReturn(null);
        when(this.env.getProperty(SendgridProvider.ENV_PASSWORD)).thenReturn(randomString());
        this.sendgridProvider.setUp();
        assertFalse(this.sendgridProvider.isConfigured());

        // Empty username
        when(this.env.getProperty(SendgridProvider.ENV_USERNAME)).thenReturn("");
        when(this.env.getProperty(SendgridProvider.ENV_PASSWORD)).thenReturn(randomString());
        this.sendgridProvider.setUp();
        assertFalse(this.sendgridProvider.isConfigured());

        // Null password
        when(this.env.getProperty(SendgridProvider.ENV_USERNAME)).thenReturn(randomString());
        when(this.env.getProperty(SendgridProvider.ENV_PASSWORD)).thenReturn(null);
        this.sendgridProvider.setUp();
        assertFalse(this.sendgridProvider.isConfigured());

        // Empty password
        when(this.env.getProperty(SendgridProvider.ENV_USERNAME)).thenReturn(randomString());
        when(this.env.getProperty(SendgridProvider.ENV_PASSWORD)).thenReturn("");
        this.sendgridProvider.setUp();
        assertFalse(this.sendgridProvider.isConfigured());

        // Everything is provided
        when(this.env.getProperty(SendgridProvider.ENV_USERNAME)).thenReturn(randomString());
        when(this.env.getProperty(SendgridProvider.ENV_PASSWORD)).thenReturn(randomString());
        this.sendgridProvider.setUp();
        assertTrue(this.sendgridProvider.isConfigured());
    }
}
