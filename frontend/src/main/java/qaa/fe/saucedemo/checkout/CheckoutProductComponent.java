package qaa.fe.saucedemo.checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import qaa.fe.common.WebElementInteraction;

public class CheckoutProductComponent extends WebElementInteraction {
    private final By productNameLocator = By.cssSelector(".inventory_item_name");
    private final By productDescriptionLocator = By.cssSelector(".inventory_item_desc");
    private final By productPriceLocator = By.cssSelector(".inventory_item_price");
    private final By productQuantityLocator = By.cssSelector(".cart_quantity");
    private final WebElement parent;

    public CheckoutProductComponent(WebElement parent, WebDriver webdriver) {
        super(webdriver);
        this.parent = parent;
    }

    public String getName() {
        return getText(waitUntilChildElementPresent(parent, productNameLocator));
    }

    public Double getPrice() {
        String price = getText(waitUntilChildElementPresent(parent, productPriceLocator)).substring(1);
        return Double.parseDouble(price);
    }

    public String getDescription() {
        return getText(waitUntilChildElementPresent(parent, productDescriptionLocator));
    }

    public int getQuantity() {
        String quantity = getText(waitUntilChildElementPresent(parent, productQuantityLocator));
        return Integer.parseInt(quantity);
    }
}
