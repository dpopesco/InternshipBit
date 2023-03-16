package qaa.fe.saucedemo.cart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import qaa.fe.exception.ProductNotFound;
import qaa.fe.saucedemo.base.BaseSauceDemo;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BaseSauceDemo<CartPage> {

    private final By cartItem = By.cssSelector(".cart_item");
    private final By continueShopping = By.id("continue-shopping");
    private final By checkout = By.id("checkout");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public CartPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrlPath() {
        return "/cart.html";
    }

    public List<CartItemComponent> getProductsInCart() {
        List<WebElement> allProducts = waitUntilElementsAreVisible(cartItem);
        return allProducts.stream()
                .map(x -> new CartItemComponent(x, webdriver))
                .collect(Collectors.toList());
    }

    public CartItemComponent getProductByName(String productName) throws ProductNotFound {
        return getProductsInCart().stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new ProductNotFound(String.format("Product %s not found", productName)));
    }

    public boolean isProductDisplayed() {
        return isLocatorDisplayed(cartItem);
    }

    public void clickOnContinueShopping() {
        click(continueShopping);
    }

    public void clickOnCheckout() {
        click(checkout);
    }

    public boolean isErrorDisplayed(String errorText) {
        return getText(errorMessage).equals(errorText);
    }
}
