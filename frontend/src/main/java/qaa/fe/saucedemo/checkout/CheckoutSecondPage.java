package qaa.fe.saucedemo.checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import qaa.fe.saucedemo.base.BaseSauceDemo;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutSecondPage extends BaseSauceDemo<CheckoutSecondPage> {
    private final By cartItem = By.cssSelector(".cart_item");
    private final By totalPrice = By.cssSelector(".summary_total_label");

    public CheckoutSecondPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrlPath() {
        return "/checkout-step-two.html";
    }

    public List<CheckoutProductComponent> getProductsInCheckout() {
        List<WebElement> allProducts = waitUntilElementsAreVisible(cartItem);
        return allProducts.stream()
                .map(x -> new CheckoutProductComponent(x, webdriver))
                .collect(Collectors.toList());
    }

    public Double getTotalPrice() {
        String price = getText(totalPrice).substring(8);
        return Double.parseDouble(price);
    }
}
