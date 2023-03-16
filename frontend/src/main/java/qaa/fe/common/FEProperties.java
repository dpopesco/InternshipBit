package qaa.fe.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@PropertySource("classpath:fe.properties")
public class FEProperties {

    @Value("${app.url}")
    private String appUrl;

    @Value("${browser.name}")
    private String browserName;

    @Value("${browser.headless}")
    private boolean browserHeadless;

    @Value("${test.grid.enable}")
    private boolean gridEnabled;

    @Value("${test.grid.hub.url}")
    private String gridUrl;
}
