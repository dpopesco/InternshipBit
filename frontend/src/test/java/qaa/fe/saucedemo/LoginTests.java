package qaa.fe.saucedemo;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.testng.AssertJUnit.assertTrue;

@Slf4j
public class LoginTests extends BaseTests {

    public static final String PASSWORD_REQUIRED = "Epic sadface: Password is required";
    public static final String USERNAME_REQUIRED = "Epic sadface: Username is required";
    public static final String USERNAME_AND_PASSWORD_REQUIRED = "Epic sadface: Username and password do not match any user in this service";

    @DataProvider(name = "validInputsForUsername")
    public Object[][] createValidInputsForUsername() {
        return new Object[][]{
                {"standard_user"},
                {"performance_glitch_user"},
                {"problem_user"}
        };
    }

    @Test(dataProvider = "validInputsForUsername")
    public void successfulLogin(String username) {
        loginPage.navigate()
                .typeUsername(username)
                .typePassword(PASSWORD)
                .clickLoginButton();
        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
    }

    @DataProvider(name = "invalidTests")
    public Object[][] createInvalidInputs() {
        return new Object[][]{
                {USERNAME, "", PASSWORD_REQUIRED},
                {"", PASSWORD, USERNAME_REQUIRED},
                {"", "", USERNAME_REQUIRED},
                {USERNAME.toUpperCase(), PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {"standard_ user", PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {" standard_user", PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {"standard_user ", PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {" ", PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {randomAlphanumeric(100), PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {XSS_INJECTION, PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {SQL_INJECTION, PASSWORD, USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, PASSWORD.toUpperCase(), USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, "secret_ sauce", USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, " secret_sauce", USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, "secret_sauce ", USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, " ", USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, randomAlphanumeric(100), USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, XSS_INJECTION, USERNAME_AND_PASSWORD_REQUIRED},
                {USERNAME, SQL_INJECTION, USERNAME_AND_PASSWORD_REQUIRED},
        };
    }

    @Test(dataProvider = "invalidTests")
    public void checkInvalidLogin(String username, String password, String error) {
        loginPage.navigate()
                .typeUsername(username)
                .typePassword(password)
                .clickLoginButton();
        assertTrue(loginPage.checkErrorIconDisplayed());
        assertTrue(loginPage.isErrorDisplayed(error));
    }
}
