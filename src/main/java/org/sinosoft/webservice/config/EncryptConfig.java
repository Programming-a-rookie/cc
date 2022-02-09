package org.sinosoft.webservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "encrypt")
@PropertySource("classpath:config/encrypt.properties")
public class EncryptConfig {

    @Value("${encrypt.id}")
    private String id;
}
