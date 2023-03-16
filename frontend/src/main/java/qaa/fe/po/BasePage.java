package qaa.fe.po;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import qaa.fe.common.FEProperties;
import qaa.fe.common.FETestContext;
import qaa.fe.common.WebElementInteraction;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public abstract class BasePage<T> extends WebElementInteraction {
    @Getter
    protected static FEProperties feProperties;

    public BasePage(WebDriver webDriver) {
        super(webDriver);

        if (feProperties == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(FETestContext.class);
            feProperties = context.getBean(FEProperties.class);
        }
    }

    protected abstract String getPageUrlPath();

    public void refreshPage() {
        String url = webdriver.getCurrentUrl();
        webdriver.navigate().to(url);
    }

    public T navigate() {
        URL fullUrl = null;
        try {
            fullUrl = new URI(feProperties.getAppUrl()).resolve(new URI(getPageUrlPath())).toURL();
        } catch (URISyntaxException | MalformedURLException me) {
            throw new RuntimeException(String.format("Invalid url: %s", fullUrl.toString()));
        }
        log.info("Navigate to {}", fullUrl);
        webdriver.get(fullUrl.toString());
        return (T) this;
    }
}
