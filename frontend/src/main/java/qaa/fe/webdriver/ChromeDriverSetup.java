package qaa.fe.webdriver;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import qaa.fe.common.FEProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;

public class ChromeDriverSetup extends AbstractDriverSetup {

    @Override
    public WebDriver getWebDriver(FEProperties properties) throws MalformedURLException {
        chromedriver().setup();
        return properties.isGridEnabled() ?
                createRemoteChromeDriver(properties) :
                new ChromeDriver(setChromeBrowserOptions(properties));
    }

    private ChromeOptions setChromeBrowserOptions(FEProperties properties) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-extensions", "ignore-certificate-errors", "--remote-allow-origins=*");
        if (properties.isBrowserHeadless()) {
            chromeOptions.addArguments("--headless=new");
        }

        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        //disable Chrome browser info bar
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        //disable profile password manager
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        chromePrefs.put("download.default_directory", getLocalDownloadLocation());
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        return chromeOptions;
    }

    private WebDriver createRemoteChromeDriver(FEProperties properties) throws MalformedURLException {
        String remoteUrl = properties.getGridUrl();

        RemoteWebDriver webDriver = new RemoteWebDriver(new URL(remoteUrl), setChromeBrowserOptions(properties));
        webDriver.setFileDetector(new LocalFileDetector());

        return webDriver;
    }

}
