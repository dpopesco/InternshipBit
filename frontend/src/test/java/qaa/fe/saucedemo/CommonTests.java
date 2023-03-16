package qaa.fe.saucedemo;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import qaa.internship.util.Bug;

import static org.testng.Assert.*;

public class CommonTests extends BaseTests {

    @BeforeMethod(alwaysRun = true)
    public void login() {
        loginPage.navigate()
                .typeUsername(USERNAME)
                .typePassword(PASSWORD)
                .clickLoginButton();
    }

    @Test
    public void openBurgerMenu() {
        inventoryPage.navigate().clickBurgerMenuIcon();

        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
        assertTrue(inventoryPage.isBurgerMenuDisplayed());
    }

    @Bug(id = "", description = "")
    @Test
    public void openAndCloseBurgerMenu() {
        inventoryPage.navigate().
                clickBurgerMenuIcon().
                clickClose();

        assertFalse(inventoryPage.isBurgerMenuDisplayed());
    }

    @Test
    public void clickLogoutFromBM() {
        inventoryPage.navigate().clickBurgerMenuIcon().clickLogout();

        assertTrue(loginPage.isOnLoginPage());
    }

    @Test(groups = "new_tab_open")
    public void clickAboutFromBM() {
        inventoryPage.navigate().clickBurgerMenuIcon().clickAbout();
        switchTab();

        assertEquals(getCurrentUrl(), ABOUT_URL);
    }

    @Test
    public void logoDisplayed() {
        inventoryPage.navigate();

        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
        assertTrue(inventoryPage.isLogoDisplayed());
        assertTrue(inventoryPage.isLogoCentered());
    }

    @Test
    public void redirectToCartWhenClickCartIcon() {
        inventoryPage.navigate().clickCartIcon();

        assertTrue(cartPage.isPageOpened(cartPage.getPageUrlPath()));
    }

    @Test(groups = "new_tab_open")
    public void redirectToFacebook() {
        inventoryPage.clickFacebookIcon();
        String currentHandle = getWebDriver().getWindowHandle();
        switchTab();

        assertTrue(inventoryPage.isPageOpened(FACEBOOK_URL));
        getWebDriver().close();
        getWebDriver().switchTo().window(currentHandle);
    }

    @Test(groups = "new_tab_open")
    public void redirectToTwitter() {
        inventoryPage.clickTwitterIcon();
        String currentHandle = getWebDriver().getWindowHandle();
        switchTab();

        assertTrue(inventoryPage.isPageOpened(TWITTER_URL));
        getWebDriver().close();
        getWebDriver().switchTo().window(currentHandle);

    }

    @Test(groups = "new_tab_open")
    public void redirectToLinkedin() {
        inventoryPage.clickLinkedinIcon();
        String currentHandle = getWebDriver().getWindowHandle();
        switchTab();

        assertTrue(inventoryPage.isPageOpened(LINKEDIN_URL));
        getWebDriver().close();
        getWebDriver().switchTo().window(currentHandle);
    }

    @Test
    public void footerText() {
        String footerText = inventoryPage.navigate().getFooterText();

        assertEquals(footerText, String.format(FOOTER_TEXT_FORMAT, currentYear));
    }
}
