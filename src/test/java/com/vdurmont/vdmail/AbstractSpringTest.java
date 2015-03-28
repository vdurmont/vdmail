package com.vdurmont.vdmail;

import com.vdurmont.vdmail.config.AppConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public abstract class AbstractSpringTest extends AbstractTest {
    @Before public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}
