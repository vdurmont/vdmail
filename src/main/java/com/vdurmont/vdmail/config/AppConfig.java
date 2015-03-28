package com.vdurmont.vdmail.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the App.
 */
@Configuration
@ComponentScan(basePackages = "com.vdurmont.vdmail.service")
public class AppConfig {}