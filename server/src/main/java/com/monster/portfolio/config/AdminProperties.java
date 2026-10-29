package com.monster.portfolio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "monster.admin")
public class AdminProperties {

    private String username = "admin";
    private String password = "admin123";
}
