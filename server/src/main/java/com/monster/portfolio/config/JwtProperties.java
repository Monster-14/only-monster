package com.monster.portfolio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "monster.jwt")
public class JwtProperties {

    private String secret;
    private long expireDays = 7;
}
