package com.monster.portfolio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "monster.upload")
public class UploadProperties {

    /**
     * Directory relative to the process working directory (usually server/).
     */
    private String dir = "uploads";
}
