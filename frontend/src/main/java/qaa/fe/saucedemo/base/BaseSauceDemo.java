package qaa.fe.saucedemo.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import qaa.fe.po.BasePage;

public abstract class BaseSauceDemo<T> extends BasePage<T> {
    private final By burgerMenuIcon = By.id("react-burger-menu-btn");
    private final By logoSwagLabs = By.cssSelector(".app_logo");
    private final By cartIcon = By.id("shopping_cart_container");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By pageTitle = By.cssSelector(".title");
    private final By burgerMenuWrap = By.cssSelector(".bm-menu-wrap");
    private final By facebookIcon = By.cssSelector("a[href='https://www.facebook.com/saucelabs']");
    private final By twitterIcon = By.cssSelector("a[href='https://twitter.com/saucelabs']");
    private final By linkedinIcon = By.cssSelector("a[href='https://www.linkedin.com/company/sauce-labs/']");
    private final By footerText = By.cssSelector(".footer_copy");

    protected BaseSauceDemo(WebDriver webDriver) {
        super(webDriver);
    }

    protected abstract String getPageUrlPath();

    public boolean isLogoDisplayed() {
        return isLocatorDisplayed(logoSwagLabs);
    }

    public boolean isLogoCentered() {
        return waitUntilElementIsVisible(logoSwagLabs).getCssValue("text-align").equals("center");
    }

    public BurgerMenuComponent clickBurgerMenuIcon() {
        click(burgerMenuIcon);
        return new BurgerMenuComponent(webdriver);
    }

    public boolean isBurgerMenuDisplayed() {
        return isLocatorDisplayed(burgerMenuWrap);
    }

    public void clickCartIcon() {
        click(cartIcon);
    }

    public boolean isCartBadgeDisplayed() {
        return isLocatorDisplayed(cartBadge);
    }

    public String getCartIconBadge() {
        return getText(cartBadge);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public void clickFacebookIcon() {
        click(facebookIcon);
    }

    public void clickTwitterIcon() {
        click(twitterIcon);
    }

    public void clickLinkedinIcon() {
        click(linkedinIcon);
    }

    public String getFooterText() {
        return getText(footerText);
    }

    public boolean isPageOpened(String url) {
        return waitUrlContains(url);
    }
}
