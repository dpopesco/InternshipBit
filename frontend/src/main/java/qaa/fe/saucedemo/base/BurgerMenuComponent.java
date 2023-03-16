package qaa.fe.saucedemo.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import qaa.fe.common.WebElementInteraction;

public class BurgerMenuComponent extends WebElementInteraction {
    private final By parent = By.cssSelector(".bm-item-list");
    private final By allItems = By.id("inventory_sidebar_link");
    private final By about = By.id("about_sidebar_link");
    private final By logout = By.id("logout_sidebar_link");
    private final By resetAppState = By.id("reset_sidebar_link");
    private final By close = By.id("react-burger-cross-btn");

    protected BurgerMenuComponent(WebDriver webdriver) {
        super(webdriver);
    }

    public void clickClose() {
        click(close);
        waitUntilElementDisappears(parent, 1);
    }

    public void clickAbout() {
        click(about);
    }

    public void clickLogout() {
        click(logout);
    }

    public void clickResetAppState() {
        click(resetAppState);
    }

    public void clickAllItems() {
        click(allItems);
    }
}
