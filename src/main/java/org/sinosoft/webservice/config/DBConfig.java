package org.sinosoft.webservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "db")
@PropertySource("classpath:config/db.properties")
public class DBConfig {

    @Value("${hive.db.driver}")
    private String driver;

    @Value("${hive.db.password}")
    private String password;

    @Value("${hive.db.url}")
    private String url;

}
