package qaa.fe.saucedemo.product.details;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import qaa.fe.saucedemo.base.BaseSauceDemo;

public class ProductDetailsPage extends BaseSauceDemo<ProductDetailsPage> {

    private final By backToProducts = By.id("back-to-products");
    private final By productName = By.cssSelector(".inventory_details_name.large_size");
    private final By productDescription = By.cssSelector(".inventory_details_desc.large_size");
    private final By productImage = By.cssSelector(".inventory_details_img");
    private final By productPrice = By.cssSelector(".inventory_details_price");
    private final By addToCart = By.cssSelector("button[id^='add-to-cart-']");
    private final By remove = By.cssSelector("button[id^='remove-']");

    public ProductDetailsPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrlPath() {
        return "/inventory-item.html";
    }

    public void clickBackToProducts() {
        click(backToProducts);
    }

    public void clickAddToCart() {
        click(addToCart);
    }

    public void clickRemove() {
        click(remove);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductDescription() {
        return getText(productDescription);
    }

    public Double getProductPrice() {
        return Double.parseDouble(getText(productPrice).substring(1));
    }

    public String getProductImageUrl() {
        return waitUntilElementIsVisible(productImage).getAttribute("src");
    }

    public boolean isAddToCartDisplayed() {
        return isLocatorDisplayed(addToCart);
    }

    public boolean isRemoveDisplayed() {
        return isLocatorDisplayed(remove);
    }
}
