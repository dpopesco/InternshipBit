package qaa.fe.saucedemo.inventory;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import qaa.fe.enums.SortBy;
import qaa.fe.exception.ProductNotFound;
import qaa.fe.saucedemo.base.BaseSauceDemo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class InventoryPage extends BaseSauceDemo<InventoryPage> {
    private final By productsLocator = By.cssSelector(".inventory_item");
    private final By productsSorting = By.cssSelector(".product_sort_container");


    public InventoryPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrlPath() {
        return "/inventory.html";
    }

    public List<ProductComponent> getProducts() {
        List<WebElement> allProducts = waitUntilElementsAreVisible(productsLocator);
        return allProducts.stream()
                .map(product -> new ProductComponent(product, webdriver))
                .collect(Collectors.toList());
    }

    public ProductComponent getProduct(String productName) throws ProductNotFound {
        return getProducts().stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new ProductNotFound(String.format("Product %s not found", productName)));
    }

    public List<String> getListOfProductsNames() {
        return getProducts().stream()
                .map(ProductComponent::getName)
                .collect(Collectors.toList());
    }

    public List<ProductComponent> getSortedAscendingProductsByName() {
        List<ProductComponent> allProducts = getProducts();
        allProducts.sort(Comparator.comparing(ProductComponent::getName));
        return allProducts;
    }

    public List<ProductComponent> getSortedDescendingProductsByName() {
        List<ProductComponent> allProducts = getProducts();
        allProducts.sort(Comparator.comparing(ProductComponent::getName, Collections.reverseOrder()));
        return allProducts;
    }

    public List<ProductComponent> getSortedAscendingProductsByPrice() {
        List<ProductComponent> allProducts = getProducts();
        allProducts.sort(Comparator.comparing(ProductComponent::getPrice));
        return allProducts;
    }

    public List<ProductComponent> getSortedDescendingProductsByPrice() {
        List<ProductComponent> allProducts = getProducts();
        allProducts.sort(Comparator.comparing(ProductComponent::getPrice, Collections.reverseOrder()));
        return allProducts;
    }

    public void clickOnSortBy(SortBy sortBy) {
        log.info("Click on sorting descending");
        Select drpSorting = new Select(waitUntilElementIsVisible(productsSorting));
        drpSorting.selectByVisibleText(sortBy.getType());
    }
}
