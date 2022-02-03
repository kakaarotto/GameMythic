package com.gm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Properties specific .
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public final Project project = new Project();

    @Data
    public static class Project {
        /**
         * Project name
         */
        private String name;

        /**
         * Project code
         */
        private String code;
        /**
         * Project baseUrl
         */
        private String baseUrl;
    }

    public Project getProject() {
        return project;
    }

    private final CorsConfiguration cors = new CorsConfiguration();

    public CorsConfiguration getCors() {
        return cors;
    }

}