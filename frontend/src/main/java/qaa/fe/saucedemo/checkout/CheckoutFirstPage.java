package qaa.fe.saucedemo.checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import qaa.fe.saucedemo.base.BaseSauceDemo;

public class CheckoutFirstPage extends BaseSauceDemo<CheckoutFirstPage> {
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By errorIcon = By.cssSelector("svg.error_icon");

    public CheckoutFirstPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrlPath() {
        return "/checkout-step-one.html";
    }

    public CheckoutFirstPage typeFirstName(String firstName) {
        clearAndType(firstNameInput, firstName);
        return this;
    }

    public CheckoutFirstPage typeLastName(String lastName) {
        clearAndType(lastNameInput, lastName);
        return this;
    }

    public CheckoutFirstPage typePostalCode(String postalCode) {
        clearAndType(postalCodeInput, postalCode);
        return this;
    }

    public void clickOnContinueButton() {
        click(continueButton);
    }

    public void clickOnCancelButton() {
        click(cancelButton);
    }

    public boolean isErrorIconDisplayed() {
        return isLocatorDisplayed(errorIcon);
    }

    public boolean isErrorDisplayed(String errorText) {
        return getText(errorMessage).equals(errorText);
    }
}
