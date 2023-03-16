package qaa.fe.saucedemo;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import qaa.fe.common.FEProperties;
import qaa.fe.common.FETestContext;
import qaa.fe.exception.ProductNotFound;
import qaa.fe.saucedemo.cart.CartPage;
import qaa.fe.saucedemo.checkout.CheckoutFirstPage;
import qaa.fe.saucedemo.checkout.CheckoutSecondPage;
import qaa.fe.saucedemo.inventory.InventoryPage;
import qaa.fe.saucedemo.inventory.ProductComponent;
import qaa.fe.saucedemo.login.LoginPage;
import qaa.fe.saucedemo.product.details.ProductDetailsPage;
import qaa.fe.webdriver.WebDriverFactory;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ContextConfiguration(classes = FETestContext.class)
public abstract class BaseTests extends AbstractTestNGSpringContextTests {
    @Autowired
    protected FEProperties properties;
    protected static final String USERNAME = "standard_user";
    protected static final String PASSWORD = "secret_sauce";
    protected static final String XSS_INJECTION = "<script>alert(\"XSS\")</script>";
    protected static final String SQL_INJECTION = "user ' OR' 5'='5";
    protected static final String FACEBOOK_URL = "https://www.facebook.com/saucelabs";
    protected static final String TWITTER_URL = "https://twitter.com/saucelabs";
    protected static final String LINKEDIN_URL = "https://www.linkedin.com";
    protected static final String ABOUT_URL = "https://saucelabs.com/";
    protected static final String SHOULD_NOT_BE_DISPLAYED = "Element SHOULD NOT be displayed but it is!";
    protected static final String SHOULD_BE_DISPLAYED = "Element SHOULD BE displayed but is not!";
    protected int currentYear = LocalDate.now().getYear();
    protected static final String FOOTER_TEXT_FORMAT = "© %d Sauce Labs. All Rights Reserved. " +
            "Terms of Service | Privacy Policy";

    public List<String> allProductsNames;

    protected String randomNameOne;
    protected String randomNameTwo;

    @Autowired
    protected WebDriverFactory webDriverFactory;

    protected final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected CartPage cartPage;
    protected CheckoutFirstPage checkoutFirstPage;
    protected CheckoutSecondPage checkoutSecondPage;

    protected ProductDetailsPage productDetailsPage;

    protected ProductComponent addProduct(String product) throws ProductNotFound {
        return inventoryPage
                .getProduct(product)
                .clickAddToCart();
    }

    @BeforeClass(alwaysRun = true)
    @Parameters("browser")
    public void initBrowser(@Optional("") String browser) throws MalformedURLException {
        webDriver.set(webDriverFactory.createWebdriver(browser));
        loginPage = new LoginPage(getWebDriver());
        inventoryPage = new InventoryPage(getWebDriver());
        cartPage = new CartPage(getWebDriver());
        productDetailsPage = new ProductDetailsPage(getWebDriver());
        checkoutFirstPage = new CheckoutFirstPage(getWebDriver());
        checkoutSecondPage = new CheckoutSecondPage(getWebDriver());
    }

    protected WebDriver getWebDriver() {
        return webDriver.get();
    }

    public String getCurrentUrl() {
        return getWebDriver().getCurrentUrl();
    }

    @AfterClass(alwaysRun = true)
    protected void closeBrowser() {
        getWebDriver().quit();
    }

    public void switchTab() {
        String currentHandle = getWebDriver().getWindowHandle();
        getWebDriver().getWindowHandles()
                .stream()
                .filter(handle -> !handle.equals(currentHandle))
                .findFirst()
                .ifPresent(handle -> getWebDriver().switchTo().window(handle));
    }

    public void setRandomNames() {
        allProductsNames = inventoryPage.getListOfProductsNames();
        Collections.shuffle(allProductsNames);
        randomNameOne = allProductsNames.get(0);
        randomNameTwo = allProductsNames.get(1);
    }
}
