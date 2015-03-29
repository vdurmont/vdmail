package com.vdurmont.vdmail.config;

import com.vdurmont.vdmail.service.PopulateService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Configuration class for the App.
 */
@Configuration
@PropertySource("classpath:vdmail.properties")
@Import(DatabaseConfig.class)
@ComponentScan(basePackages = {
        "com.vdurmont.vdmail.exception",
        "com.vdurmont.vdmail.mapper",
        "com.vdurmont.vdmail.service"
})
public class AppConfig {
    @Inject private PopulateService populateService;

    @PostConstruct
    public void setUp() {
        this.populateService.populate();
    }
}