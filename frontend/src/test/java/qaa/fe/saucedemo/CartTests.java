package qaa.fe.saucedemo;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import qaa.fe.exception.ProductNotFound;
import qaa.fe.models.Product;
import qaa.fe.saucedemo.cart.CartItemComponent;
import qaa.internship.util.Bug;

import static org.testng.Assert.*;

public class CartTests extends BaseTests {
    public static final String CART_EMPTY = "Error: There is nothing to checkout";

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
    public void checkAddedProductWithCartAddition() throws ProductNotFound {

        Product productModel = new Product(addProduct(randomNameOne));
        cartPage.clickCartIcon();

        assertTrue(cartPage.isPageOpened(cartPage.getPageUrlPath()));
        CartItemComponent firstProduct = cartPage.getProductsInCart().get(0);
        assertEquals(firstProduct.getName(), productModel.getName());
        assertEquals(firstProduct.getDescription(), productModel.getDescription());
        assertEquals(firstProduct.getPrice(), productModel.getPrice());
        assertEquals(firstProduct.getQuantity(), 1);
    }

    @Test
    public void addTwoProductsInCart() throws ProductNotFound {
        Product productModel1 = new Product(addProduct(randomNameOne));
        Product productModel2 = new Product(addProduct(randomNameTwo));
        cartPage.clickCartIcon();

        CartItemComponent firstProduct = cartPage.getProductsInCart().get(0);
        assertEquals(firstProduct.getName(), productModel1.getName());
        assertEquals(firstProduct.getDescription(), productModel1.getDescription());
        assertEquals(firstProduct.getPrice(), productModel1.getPrice());
        assertEquals(firstProduct.getQuantity(), 1);

        CartItemComponent secondProduct = cartPage.getProductsInCart().get(1);
        assertEquals(secondProduct.getName(), productModel2.getName());
        assertEquals(secondProduct.getDescription(), productModel2.getDescription());
        assertEquals(secondProduct.getPrice(), productModel2.getPrice());
        assertEquals(secondProduct.getQuantity(), 1);
    }

    @Test
    public void addProductsAndRemoveByProductName() throws ProductNotFound {
        Product productModel1 = new Product(addProduct(randomNameOne));
        addProduct(randomNameTwo);
        cartPage.clickCartIcon();

        cartPage.getProductByName(productModel1.getName()).clickRemove();
        assertEquals(cartPage.getProductsInCart().size(), 1);
    }

    @Test
    public void addProductThenRemoveIt() throws ProductNotFound {
        addProduct(randomNameOne);
        cartPage.clickCartIcon();

        cartPage.getProductsInCart().get(0).clickRemove();
        assertFalse(cartPage.isProductDisplayed());
    }

    @Test
    public void addProductsAndContinueShopping() throws ProductNotFound {
        addProduct(randomNameOne);

        cartPage.clickCartIcon();
        int numberOfProducts = cartPage.getProductsInCart().size();
        cartPage.clickOnContinueShopping();

        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
        assertEquals(cartPage.getCartIconBadge(), String.valueOf(numberOfProducts));
    }

    @Test
    public void addProductsContinueShoppingAndAddMoreProducts() throws ProductNotFound {
        addProduct(randomNameOne);

        cartPage.clickCartIcon();
        cartPage.clickOnContinueShopping();

        addProduct(randomNameTwo);

        cartPage.clickCartIcon();
        int numberOfProducts = cartPage.getProductsInCart().size();
        assertEquals(cartPage.getCartIconBadge(), String.valueOf(numberOfProducts));
    }

    @Test
    public void checkCartPageAfterReset() throws ProductNotFound {
        addProduct(randomNameOne);

        inventoryPage.clickBurgerMenuIcon().clickResetAppState();
        inventoryPage.clickCartIcon();

        assertFalse(cartPage.isProductDisplayed());
    }

    @Test
    public void checkPageTitle() {
        inventoryPage.clickCartIcon();
        assertEquals(cartPage.getPageTitle(), "Your Cart");
    }

    @Bug(id = "", description = "Cart allows to checkout with empty cart")
    @Test
    public void checkoutWithEmptyCart() {
        cartPage.clickCartIcon();
        cartPage.clickOnCheckout();

        assertTrue(getCurrentUrl().contains(cartPage.getPageUrlPath()));
        assertTrue(cartPage.isErrorDisplayed(CART_EMPTY));
    }
}
