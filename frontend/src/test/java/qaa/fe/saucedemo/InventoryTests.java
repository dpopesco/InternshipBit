package qaa.fe.saucedemo;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import qaa.fe.enums.SortBy;
import qaa.fe.exception.ProductNotFound;
import qaa.fe.saucedemo.inventory.ProductComponent;

import java.util.Arrays;

import static org.testng.Assert.*;

public class InventoryTests extends BaseTests {

    @BeforeMethod(alwaysRun = true)
    public void login(ITestResult result) {
        loginPage.navigate()
                .typeUsername(USERNAME)
                .typePassword(PASSWORD)
                .clickLoginButton();
        Arrays.stream(result.getMethod().getGroups())
                .filter(group -> group.equals("add_to_cart") || group.equals("open_product_page"))
                .findFirst()
                .ifPresent(group -> setRandomNames()
                );
        assertTrue(inventoryPage.isPageOpened(inventoryPage.getPageUrlPath()));
    }

    @AfterMethod(alwaysRun = true)
    public void resetAppState(ITestResult result) {
        Arrays.stream(result.getMethod().getGroups())
                .filter(group -> group.equals("add_to_cart"))
                .findFirst()
                .ifPresent(group -> inventoryPage.clickBurgerMenuIcon().clickResetAppState());
    }

    @Test(groups = "add_to_cart")
    public void addProductToCart() throws ProductNotFound {
        ProductComponent product = addProduct(randomNameOne);

        assertEquals(inventoryPage.getCartIconBadge(), "1");
        assertTrue(product.isRemoveDisplayed(), SHOULD_BE_DISPLAYED);
    }

    @Test(groups = "add_to_cart")
    public void addTwoProductsToCart() throws ProductNotFound {
        ProductComponent productOne = addProduct(randomNameOne);

        ProductComponent productTwo = addProduct(randomNameTwo);

        assertEquals(inventoryPage.getCartIconBadge(), "2");
        assertTrue(productOne.isRemoveDisplayed(), SHOULD_BE_DISPLAYED);
        assertTrue(productTwo.isRemoveDisplayed(), SHOULD_BE_DISPLAYED);
    }

    @Test(groups = "add_to_cart")
    public void addProductToCartAndRemoveIt() throws ProductNotFound {
        ProductComponent product = addProduct(randomNameOne).clickRemove();

        assertTrue(product.isAddToCartDisplayed(), SHOULD_BE_DISPLAYED);
        assertFalse(inventoryPage.isCartBadgeDisplayed(), SHOULD_NOT_BE_DISPLAYED);
    }

    @Test
    public void checkPageIsSortedByNameAscendingByDefault() {
        inventoryPage.navigate();
        assertEquals(inventoryPage.getSortedAscendingProductsByName(), inventoryPage.getProducts());
    }

    @Test
    public void checkPageIsSortedByNameAscending() {
        inventoryPage.navigate().clickOnSortBy(SortBy.PRICE_DESC);
        assertEquals(inventoryPage.getSortedDescendingProductsByPrice(), inventoryPage.getProducts());
        inventoryPage.clickOnSortBy(SortBy.NAME_ASC);
        assertEquals(inventoryPage.getSortedAscendingProductsByName(), inventoryPage.getProducts());
    }

    @Test
    public void checkPageIsSortedByNameDescending() {
        inventoryPage.navigate().clickOnSortBy(SortBy.NAME_DESC);
        assertEquals(inventoryPage.getSortedDescendingProductsByName(), inventoryPage.getProducts());
    }

    @Test
    public void checkPageIsSortedByPriceAscending() {
        inventoryPage.navigate().clickOnSortBy(SortBy.PRICE_ASC);
        assertEquals(inventoryPage.getSortedAscendingProductsByPrice(), inventoryPage.getProducts());
    }

    @Test
    public void checkPageIsSortedByPriceDescending() {
        inventoryPage.navigate().clickOnSortBy(SortBy.PRICE_DESC);
        assertEquals(inventoryPage.getSortedDescendingProductsByPrice(), inventoryPage.getProducts());
    }

    @Test
    public void checkAllProductsHaveAddToCart() throws ProductNotFound {
        inventoryPage.navigate();
        for (String productName : allProductsNames) {
            assertTrue(inventoryPage.getProduct(productName).isAddToCartDisplayed(), SHOULD_BE_DISPLAYED);
        }
    }

    @Test(groups = "open_product_page")
    public void clickResetAppStateFromBM() throws ProductNotFound, InterruptedException {
        addProduct(randomNameOne);
        inventoryPage.clickBurgerMenuIcon().clickResetAppState();
        inventoryPage.refreshPage();
        ProductComponent product = inventoryPage.getProduct(randomNameOne);

        assertFalse(inventoryPage.isCartBadgeDisplayed(), SHOULD_NOT_BE_DISPLAYED);
        assertTrue(product.isAddToCartDisplayed(), SHOULD_BE_DISPLAYED);
    }

    @Test(groups = "open_product_page")
    public void clickProductName() throws ProductNotFound {
        inventoryPage.navigate()
                .getProduct(randomNameOne)
                .clickProductName();

        assertTrue(productDetailsPage.isPageOpened(productDetailsPage.getPageUrlPath()));
    }

    @Test(groups = "open_product_page")
    public void clickProductImage() throws ProductNotFound {
        inventoryPage.navigate()
                .getProduct(randomNameOne)
                .clickProductImage();

        assertTrue(productDetailsPage.isPageOpened(productDetailsPage.getPageUrlPath()));
    }
}
