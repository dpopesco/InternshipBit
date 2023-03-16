package qaa.fe.webdriver;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qaa.fe.common.FEProperties;

import java.net.MalformedURLException;

@Component
public class WebDriverFactory {

    @Autowired
    private FEProperties properties;
    private static final String CHROME = "chrome";
    private static final String FIREFOX = "firefox";
    private final ThreadLocal<WebDriver> webdriver = new ThreadLocal<>();

    public WebDriver createWebdriver(String browserName) throws MalformedURLException {
        webdriver.set(createWebdriverBasedOnBrowserName(browserName));
        webdriver.get().manage().window().maximize();
        return webdriver.get();
    }

    private WebDriver createWebdriverBasedOnBrowserName(String browserName) throws MalformedURLException {
        switch (browserName.isEmpty() ? properties.getBrowserName() : browserName) {
            case FIREFOX:
                return new FirefoxDriverSetup().getWebDriver(properties);
            case CHROME:
                return new ChromeDriverSetup().getWebDriver(properties);
            default:
                throw new RuntimeException(
                        String.format("Wrong browser name {%s}", properties.getBrowserName()));
        }
    }
}
