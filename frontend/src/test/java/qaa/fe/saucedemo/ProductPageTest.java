package qaa.fe.saucedemo;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import qaa.fe.exception.ProductNotFound;
import qaa.fe.models.Product;
import qaa.fe.saucedemo.inventory.ProductComponent;
import qaa.internship.util.Bug;

import java.util.Arrays;
import java.util.Random;

import static org.testng.Assert.*;

public class ProductPageTest extends BaseTests {
    Random random = new Random();
    Product product;

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) throws ProductNotFound {
        loginPage.navigate()
                .typeUsername(USERNAME)
                .typePassword(PASSWORD)
                .clickLoginButton();
        ProductComponent productComponent = inventoryPage.getProduct(inventoryPage.getListOfProductsNames().
                get(random.nextInt(5)));
        Arrays.stream(result.getMethod().getGroups())
                .filter(group -> group.equals("product_add_to_cart_clicked"))
                .findFirst()
                .ifPresent(group -> productComponent.clickAddToCart());
        product = new Product(productComponent.getName(),
                productComponent.getDescription(),
                productComponent.getImageUrl(),
                productComponent.getPrice(),
                productComponent.isAddToCartDisplayed());
        productComponent.clickProductName();
        assertTrue(productDetailsPage.isPageOpened(productDetailsPage.getPageUrlPath()));
    }

    @AfterMethod(groups = "remove/add clicked")
    public void resetAppState(ITestResult result) {
        Arrays.stream(result.getMethod().getGroups())
                .filter(group -> group.equals("remove/add clicked") || group.equals("product_add_to_cart_clicked"))
                .findFirst()
                .ifPresent(group -> inventoryPage.navigate().clickBurgerMenuIcon().clickResetAppState());
    }

    @Test
    public void checkBackToProducts() {

        productDetailsPage.clickBackToProducts();

        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
    }

    @Test
    public void checkProductInformationAreCorrect() {
        assertEquals(productDetailsPage.getProductName(), product.getName());
        assertEquals(productDetailsPage.getProductDescription(), product.getDescription());
        assertEquals(productDetailsPage.getProductPrice(), product.getPrice());
        assertEquals(productDetailsPage.getProductImageUrl(), product.getImageUrl());
        assertEquals(productDetailsPage.isAddToCartDisplayed(), product.isAddToCart());
    }

    @Test
    public void addProductToCartAndRemoveIt() {
        productDetailsPage.clickAddToCart();

        assertTrue(productDetailsPage.isRemoveDisplayed(), SHOULD_BE_DISPLAYED);
        assertTrue(productDetailsPage.isCartBadgeDisplayed(), SHOULD_BE_DISPLAYED);
        assertEquals(productDetailsPage.getCartIconBadge(), "1");

        productDetailsPage.clickRemove();

        assertFalse(productDetailsPage.isRemoveDisplayed(), SHOULD_NOT_BE_DISPLAYED);
        assertTrue(productDetailsPage.isAddToCartDisplayed(), SHOULD_BE_DISPLAYED);
        assertFalse(productDetailsPage.isCartBadgeDisplayed(), SHOULD_NOT_BE_DISPLAYED);
    }

    @Test
    @Bug(id = "", description = "RESET APP STATE button from Burger Menu don't reset product button to ADD TO CART")
    public void resetAppFromProductPage() {
        productDetailsPage.clickAddToCart();
        productDetailsPage.clickBurgerMenuIcon().clickResetAppState();
        productDetailsPage.refreshPage();

        assertFalse(productDetailsPage.isCartBadgeDisplayed(), SHOULD_NOT_BE_DISPLAYED);
        assertFalse(productDetailsPage.isRemoveDisplayed(), SHOULD_NOT_BE_DISPLAYED);
        assertTrue(productDetailsPage.isAddToCartDisplayed(), SHOULD_BE_DISPLAYED);
    }

    @Test(groups = "product_add_to_cart_clicked")
    public void getProductPageRemoveDisplayed() {
        assertTrue(productDetailsPage.isRemoveDisplayed(), SHOULD_BE_DISPLAYED);
    }

    @Test
    public void clickAllItemsGoToInventory() {

        productDetailsPage.clickBurgerMenuIcon().clickAllItems();

        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
    }

    @Test(groups = "remove/add clicked")
    public void verifyIfRemoveRemainDisplayedOnInventory() throws ProductNotFound {
        productDetailsPage.clickAddToCart();
        productDetailsPage.clickBackToProducts();

        assertTrue(inventoryPage.getProduct(product.getName()).isRemoveDisplayed(), SHOULD_BE_DISPLAYED);
    }
}
