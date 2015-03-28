package com.vdurmont.vdmail.service.mailprovider;

import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.vdurmont.vdmail.AbstractSpringTest;
import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.model.User;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;

import java.util.List;

import static com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class MandrillProviderTest extends AbstractSpringTest {
    @InjectMocks private MandrillProvider mandrillProvider;

    @Mock private Environment env;

    @Test public void setUp_tests() {
        // Null apiKey
        when(this.env.getProperty(MandrillProvider.ENV_API_KEY)).thenReturn(null);
        this.mandrillProvider.setUp();
        assertFalse(this.mandrillProvider.isEnabled());

        // Empty apiKey
        when(this.env.getProperty(MandrillProvider.ENV_API_KEY)).thenReturn("");
        this.mandrillProvider.setUp();
        assertFalse(this.mandrillProvider.isEnabled());

        // Provided apiKey
        when(this.env.getProperty(MandrillProvider.ENV_API_KEY)).thenReturn(randomString());
        this.mandrillProvider.setUp();
        assertTrue(this.mandrillProvider.isEnabled());
    }

    @Test public void toMandrillMessage_converts_the_email() {
        // GIVEN
        User user = generateUser();
        Email email = generateValidEmail();

        // WHEN
        MandrillMessage message = MandrillProvider.toMandrillMessage(user, email);

        // THEN
        // Sender
        assertEquals(user.getAddress(), message.getFromEmail());
        assertEquals(user.getName(), message.getFromName());

        // Recipients
        List<Recipient> tos = message.getTo();
        Recipient to = tos.get(0);
        assertEquals(1, tos.size());
        assertEquals(email.getToName(), to.getName());
        assertEquals(email.getToAddress(), to.getEmail());
        assertEquals(Recipient.Type.TO, to.getType());

        // Body
        assertEquals(email.getSubject(), message.getSubject());
        assertEquals(email.getContent(), message.getText());
    }
}
