package qaa.fe.saucedemo;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import qaa.fe.exception.ProductNotFound;
import qaa.fe.models.Product;
import qaa.fe.saucedemo.checkout.CheckoutProductComponent;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CheckoutSecondPageTests extends BaseTests {

    @BeforeMethod(alwaysRun = true)
    public void login() {
        loginPage.navigate()
                .typeUsername(USERNAME)
                .typePassword(PASSWORD)
                .clickLoginButton();

        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
        setRandomNames();
    }

    @AfterMethod(alwaysRun = true)
    public void resetAppState() {
        cartPage.clickBurgerMenuIcon().clickResetAppState();
    }

    @Test
    public void checkProductAddedInCartSameAsProductOnCheckoutOverview() throws ProductNotFound {

        Product cartProduct = new Product(addProduct(randomNameOne));
        cartPage.clickCartIcon();

        int cartQuantity = cartPage.getProductsInCart().get(0).getQuantity();
        cartPage.clickOnCheckout();

        checkoutFirstPage.navigate()
                .typeFirstName(randomAlphanumeric(4))
                .typeLastName(randomAlphanumeric(4))
                .typePostalCode(randomAlphanumeric(3))
                .clickOnContinueButton();

        assertTrue(checkoutSecondPage.isPageOpened(checkoutSecondPage.getPageUrlPath()));
        CheckoutProductComponent checkoutProduct = checkoutSecondPage.getProductsInCheckout().get(0);
        Product productCheckout = new Product(checkoutProduct);
        assertEquals(productCheckout, cartProduct);
        assertEquals(checkoutProduct.getQuantity(), cartQuantity);
    }

    @Test
    public void checkTotalPriceOfProductsInCheckout() throws ProductNotFound {

        Product cartProduct = new Product(addProduct(randomNameOne));
        Product cartProduct2 = new Product(addProduct(randomNameTwo));
        cartPage.clickCartIcon();

        double totalPriceInCart = cartProduct.getPrice() + cartProduct2.getPrice();

        int cartQuantity = cartPage.getProductsInCart().get(0).getQuantity();
        cartPage.clickOnCheckout();

        checkoutFirstPage.navigate()
                .typeFirstName(randomAlphanumeric(4))
                .typeLastName(randomAlphanumeric(4))
                .typePostalCode(randomAlphanumeric(3))
                .clickOnContinueButton();

        assertTrue(checkoutSecondPage.isPageOpened(checkoutSecondPage.getPageUrlPath()));
        CheckoutProductComponent checkoutProduct = checkoutSecondPage.getProductsInCheckout().get(0);
        Product productCheckout = new Product(checkoutProduct);

        assertEquals(productCheckout, cartProduct);
        assertEquals(checkoutProduct.getQuantity(), cartQuantity);
        Object roundedPrice = Math.round(totalPriceInCart + totalPriceInCart * 0.08);
        assertEquals(Math.round(checkoutSecondPage.getTotalPrice()), roundedPrice);
    }
}
