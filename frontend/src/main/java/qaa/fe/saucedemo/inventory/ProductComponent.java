package qaa.fe.saucedemo.inventory;

import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import qaa.fe.common.WebElementInteraction;
import qaa.fe.saucedemo.product.details.ProductDetailsPage;

@EqualsAndHashCode(callSuper = false)
public class ProductComponent extends WebElementInteraction {
    private final By productNameLocator = By.cssSelector(".inventory_item_name");
    private final By productDescriptionLocator = By.cssSelector(".inventory_item_desc");
    private final By productPriceLocator = By.cssSelector(".inventory_item_price");
    private final By productImageLocator = By.cssSelector("img[class='inventory_item_img']");
    private final By productAddToCart = By.cssSelector("button[id^='add-to-cart']");
    private final By productRemove = By.cssSelector("button[id^='remove-']");
    private final WebElement parent;

    public ProductComponent(WebElement parent, WebDriver webDriver) {
        super(webDriver);
        this.parent = parent;
    }

    public ProductComponent clickAddToCart() {
        click(waitUntilChildElementPresent(parent, productAddToCart));
        return (this);
    }

    public boolean isRemoveDisplayed() {
        try {
            return isElementDisplayed(parent.findElement(productRemove));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isAddToCartDisplayed() {
        try {
            return isElementDisplayed(parent.findElement(productAddToCart));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public ProductComponent clickRemove() {
        click(waitUntilChildElementPresent(parent, productRemove));
        return (this);
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

    public String getImageUrl() {
        return waitUntilChildElementPresent(parent, productImageLocator).getAttribute("src");
    }

    public ProductDetailsPage clickProductName() {
        waitUntilChildElementPresent(parent, productNameLocator).click();
        return new ProductDetailsPage(webdriver);
    }

    public ProductDetailsPage clickProductImage() {
        waitUntilChildElementPresent(parent, productImageLocator).click();
        return new ProductDetailsPage(webdriver);
    }
}
