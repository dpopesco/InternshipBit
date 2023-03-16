package qaa.fe.common;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@Slf4j
public abstract class WebElementInteraction {

    private static final long EXPLICIT_WAIT_TIME = 20;
    private static final long POLLING_TIME_MILLIS = 500;

    protected final WebDriver webdriver;

    protected WebElementInteraction(WebDriver webdriver) {
        this.webdriver = webdriver;
    }

    protected WebElement waitUntilElementIsVisible(By locator) {
        return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until(visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilElementIsVisible(WebElement element) {
        return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until(visibilityOf(element));
    }

    protected WebElement waitUntilElementClickable(By locator) {
        return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until(elementToBeClickable(locator));
    }

    protected void click(By locator) {
        log.info("Click on button {}", locator.toString());
        waitUntilElementClickable(locator).click();
    }

    protected void click(WebElement element) {
        waitUntilElementClickable(element).click();
    }

    protected WebElement waitUntilElementClickable(WebElement element) {
        return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until(elementToBeClickable(element));
    }

    protected void waitUntilElementDisappears(WebElement element, int seconds) {
        try {
            setWaitingTime(seconds, POLLING_TIME_MILLIS).until(invisibilityOf(element));
        } catch (TimeoutException e) {
            log.info("Element {} is not displayed anymore", getLocatorForWebElement(element));
        }
    }

    protected void waitUntilElementDisappears(By locator, int seconds) {
        try {
            setWaitingTime(seconds, POLLING_TIME_MILLIS).until(invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.info("Element {} is not displayed anymore", locator);
        }
    }

    protected List<WebElement> waitUntilElementsAreVisible(By element) {
        return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until(visibilityOfAllElementsLocatedBy(element));
    }

    protected String getLocatorForWebElement(WebElement element) {
        try {
            return element.toString().substring(element.toString().indexOf("->"));
        } catch (Exception e) {
            return element.toString();
        }
    }

    protected boolean isLocatorDisplayed(By locator) {
        try {
            return webdriver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException | NullPointerException
                exception) {
            log.info("Locator {} is not displayed", locator);
        }
        return false;
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException | NullPointerException
                exception) {
            log.info("Element {} is not displayed", element);
        }
        return false;
    }

    protected boolean waitUrlContains(String url) {
        return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until(ExpectedConditions.urlContains(url));
    }

    protected boolean waitUrlEqual(String url) {
        return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until(ExpectedConditions.urlToBe(url));
    }

    protected WebElement waitUntilChildElementPresent(WebElement parentElement, By childLocator) {
        try {
            return setWaitingTime(EXPLICIT_WAIT_TIME, POLLING_TIME_MILLIS).until
                    (ExpectedConditions.presenceOfNestedElementLocatedBy(parentElement, childLocator));
        } catch (TimeoutException | StaleElementReferenceException noSuchElement) {
            throw new TimeoutException(String.format
                    ("Child element %s was not found in the given %d seconds", childLocator, EXPLICIT_WAIT_TIME),
                    noSuchElement.getCause());
        }
    }

    protected void clearAndType(By locator, String value) {
        log.info("Type value {} in field {}", value, locator.toString());
        WebElement input = waitUntilElementIsVisible(locator);
        input.clear();
        input.sendKeys(value);
    }

    protected String getText(By selector) {
        return waitUntilElementIsVisible(selector).getText();
    }

    protected String getText(WebElement element) {
        return waitUntilElementIsVisible(element).getText();
    }

    protected String getChildText(WebElement parent, By childSelector) {
        return waitUntilChildElementPresent(parent, childSelector).getText();
    }

    protected String getCssValue(By locator, String field) {
        return waitUntilElementIsVisible(locator).getCssValue(field);
    }

    protected FluentWait<WebDriver> setWaitingTime(long timeOutInSeconds, long pollingTimeInMillis) {

        return new WebDriverWait(webdriver, Duration.ofSeconds(timeOutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingTimeInMillis))
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .ignoring(StaleElementReferenceException.class);
    }
}
