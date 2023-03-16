package qaa.fe.saucedemo.login;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import qaa.fe.po.BasePage;

@Slf4j
public class LoginPage extends BasePage<LoginPage> {
    private final By userNameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorIcon = By.cssSelector("svg.error_icon");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected String getPageUrlPath() {
        return "";
    }

    public boolean isOnLoginPage() {
        return waitUrlEqual(getFeProperties().getAppUrl());
    }

    public LoginPage typeUsername(String username) {
        log.info("Type username {}", username);
        clearAndType(userNameInput, username);
        return this;
    }

    public LoginPage typePassword(String psw) {
        log.info("Type password {}", psw);
        clearAndType(passwordInput, psw);
        return this;
    }

    public void clickLoginButton() {
        log.info("Click on login button");
        click(loginButton);
    }

    public boolean checkErrorIconDisplayed() {
        return isLocatorDisplayed(errorIcon);
    }

    public boolean isErrorDisplayed(String errorText) {
        return getText(errorMessage).equals(errorText);
    }
}
