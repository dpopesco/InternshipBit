package qaa.fe.webdriver;

import org.openqa.selenium.WebDriver;
import qaa.fe.common.FEProperties;

import java.io.File;
import java.net.MalformedURLException;

public abstract class AbstractDriverSetup {

    public abstract WebDriver getWebDriver(FEProperties properties) throws MalformedURLException;

    protected String getLocalDownloadLocation() {
        return System.getProperty("user.dir") + File.separator;
    }
}
