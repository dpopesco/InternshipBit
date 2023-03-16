package qaa.fe.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import qaa.fe.common.FEProperties;

import java.net.MalformedURLException;
import java.net.URL;

import static io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver;

public class FirefoxDriverSetup extends AbstractDriverSetup {


    @Override
    public WebDriver getWebDriver(FEProperties properties) throws MalformedURLException {
        firefoxdriver().setup();
        return properties.isGridEnabled() ?
                createRemoteFirefoxDriver(properties) :
                new FirefoxDriver(setFirefoxBrowserOptions(properties));
    }

    private FirefoxOptions setFirefoxBrowserOptions(FEProperties properties) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addPreference("browser.download.dir", getLocalDownloadLocation());
        if (properties.isBrowserHeadless()) {
            firefoxOptions.addArguments("-headless");
        }
        firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
        firefoxOptions.addPreference("browser.download.manager.focusWhenStarting", false);
        firefoxOptions.addPreference("browser.download.useDownloadDir", true);
        firefoxOptions.addPreference("browser.helperApps.alwaysAsk.force", false);
        firefoxOptions.addPreference("browser.download.manager.closeWhenDone", true);
        firefoxOptions.addPreference("browser.download.manager.showAlertOnComplete", false);
        firefoxOptions.setAcceptInsecureCerts(true);

        return firefoxOptions;
    }

    private WebDriver createRemoteFirefoxDriver(FEProperties properties) throws MalformedURLException {
        String remoteUrl = properties.getGridUrl();
        RemoteWebDriver webDriver = new RemoteWebDriver(new URL(remoteUrl), setFirefoxBrowserOptions(properties));
        webDriver.setFileDetector(new LocalFileDetector());

        return webDriver;
    }
}
