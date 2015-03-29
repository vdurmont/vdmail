package com.vdurmont.vdmail.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the App.
 */
@Configuration
@PropertySource("classpath:vdmail.properties")
@Import(DatabaseConfig.class)
@ComponentScan(basePackages = {
        "com.vdurmont.vdmail.mapper",
        "com.vdurmont.vdmail.service"
})
public class AppConfig {}