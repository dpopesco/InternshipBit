package qaa.fe.saucedemo;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import qaa.fe.exception.ProductNotFound;
import qaa.internship.util.Bug;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertTrue;

public class CheckoutFirstPageTests extends BaseTests {
    public static final String FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String LAST_NAME_REQUIRED = "Error: Last Name is required";
    public static final String POSTAL_CODE_REQUIRED = "Error: Postal Code is required";
    public static final String ENTERED_INPUT_IS_TOO_LONG = "Error: Entered input is longer than maximum length allowed of 50 characters";
    public static final String ENTERED_FIRST_NAME_IS_TOO_SHORT = "Error: First Name requires input longer than 1 characters";
    public static final String ENTERED_LAST_NAME_IS_TOO_SHORT = "Error: Last Name requires input longer than 1 characters";
    public static final String ENTERED_POSTAL_CODE_IS_TOO_SHORT = "Error: Postal code requires input longer than 2 characters";

    @BeforeMethod(alwaysRun = true)
    public void login() throws ProductNotFound {
        loginPage.navigate()
                .typeUsername(USERNAME)
                .typePassword(PASSWORD)
                .clickLoginButton();

        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
        setRandomNames();

        addProduct(randomNameOne);

        cartPage.clickCartIcon();

        cartPage.clickOnCheckout();
        assertTrue(checkoutFirstPage.isPageOpened(checkoutFirstPage.getPageUrlPath()));
    }

    @AfterMethod(alwaysRun = true)
    public void resetAppState() {
        cartPage.clickBurgerMenuIcon().clickResetAppState();
    }

    @DataProvider(name = "validTests")
    public Object[][] createValidInputs() {
        return new Object[][]{
                {randomAlphanumeric(2), randomAlphanumeric(2), randomAlphanumeric(4)},
                {randomAlphanumeric(50), randomAlphanumeric(50), randomAlphanumeric(50)}
        };
    }

    @Test(dataProvider = "validTests")
    public void checkValidInformationProvided(String firstName, String lastName, String postalCode) {
        checkoutFirstPage.navigate()
                .typeFirstName(firstName)
                .typeLastName(lastName)
                .typePostalCode(postalCode)
                .clickOnContinueButton();
        assertTrue(checkoutSecondPage.isPageOpened(checkoutSecondPage.getPageUrlPath()));
    }

    @DataProvider(name = "invalidTests")
    public Object[][] createInvalidInputs() {
        return new Object[][]{
                {"", "", "", FIRST_NAME_REQUIRED},
                {randomAlphanumeric(2), "", "", LAST_NAME_REQUIRED},
                {randomAlphanumeric(2), randomAlphanumeric(2), "", POSTAL_CODE_REQUIRED},
                {XSS_INJECTION, randomAlphanumeric(2), randomAlphanumeric(3), FIRST_NAME_REQUIRED},
                {SQL_INJECTION, randomAlphanumeric(2), randomAlphanumeric(3), FIRST_NAME_REQUIRED},
                {randomAlphanumeric(2), XSS_INJECTION, randomAlphanumeric(3), LAST_NAME_REQUIRED},
                {randomAlphanumeric(2), SQL_INJECTION, randomAlphanumeric(3), LAST_NAME_REQUIRED},
                {randomAlphanumeric(2), randomAlphanumeric(2), XSS_INJECTION, POSTAL_CODE_REQUIRED},
                {randomAlphanumeric(2), randomAlphanumeric(2), SQL_INJECTION, POSTAL_CODE_REQUIRED},
                {" ", " ", " ", FIRST_NAME_REQUIRED},
                {randomAlphanumeric(2), " ", " ", LAST_NAME_REQUIRED},
                {randomAlphanumeric(2), randomAlphanumeric(2), " ", POSTAL_CODE_REQUIRED},
                {randomAlphanumeric(1), randomAlphanumeric(2), randomAlphanumeric(3), ENTERED_FIRST_NAME_IS_TOO_SHORT},
                {randomAlphanumeric(2), randomAlphanumeric(1), randomAlphanumeric(3), ENTERED_LAST_NAME_IS_TOO_SHORT},
                {randomAlphanumeric(2), randomAlphanumeric(2), randomAlphanumeric(3), ENTERED_POSTAL_CODE_IS_TOO_SHORT},
                {randomAlphanumeric(51), randomAlphanumeric(51), randomAlphanumeric(51), ENTERED_INPUT_IS_TOO_LONG},
        };
    }
    @Bug(id = "", description = "Checkout first page accepts xss and sql injection")
    @Test(dataProvider = "invalidTests")
    public void checkInvalidInformationProvided(String firstName, String lastName, String postalCode, String error) {
        checkoutFirstPage.navigate()
                .typeFirstName(firstName)
                .typeLastName(lastName)
                .typePostalCode(postalCode)
                .clickOnContinueButton();
        assertTrue(getCurrentUrl().contains(checkoutFirstPage.getPageUrlPath()));
        assertTrue(checkoutFirstPage.isErrorIconDisplayed());
        assertTrue(checkoutFirstPage.isErrorDisplayed(error));
    }

    @Test
    public void clickOnCancelWhileOnCheckoutInformationPage() {
        checkoutFirstPage.clickOnCancelButton();
        assertTrue(cartPage.isPageOpened(cartPage.getPageUrlPath()));
    }
}
