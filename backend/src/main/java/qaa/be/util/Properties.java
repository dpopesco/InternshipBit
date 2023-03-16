package qaa.be.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:config.properties")
public class Properties {

    @Value("${project.uri}")
    private String URI;

    @Value("${project.id}")
    private String APP_ID;
}

