package qaa.be.dummyapi.util;

import org.testng.annotations.DataProvider;

import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.*;


public class PaginationDataProvider {

    protected static Random rand = new Random();

    @DataProvider(name = "invalid_ids")
    public static Object[][] createDataIds() {
        return new Object[][]{
                {randomAlphanumeric(23)},
                {randomAlphanumeric(24)},
                {randomNumeric(24)},
                {randomAlphabetic(24)},
                {randomAlphanumeric(25)},
                {""},
                {" "}
        };
    }

    @DataProvider(name = "valid_page_values")
    public static Object[][] createValidDataPageParam() {
        return new Integer[][]{
                {0}, {999}, {rand.nextInt(898) + 100}
        };
    }

    @DataProvider(name = "invalid_page_values")
    public static Object[][] createInvalidDataPageParam() {
        return new Object[][]{
                {-1}, {1000}, {"something"}, {45.564}, {false}
        };
    }

    @DataProvider(name = "valid_limit_values")
    public static Object[][] createValidDataLimitParam() {
        return new Integer[][]{
                {5}, {50}, {rand.nextInt(44) + 6}
        };
    }

    @DataProvider(name = "invalid_limit_values")
    public static Object[][] createInvalidDataLimitParam() {
        return new Object[][]{
                {4}, {51}, {"something"}, {45.564}, {false}, {-1}
        };
    }

    @DataProvider(name = "valid_limit_and_page_values")
    public static Object[][] createValidDataLimitAndPageParam() {
        return new Integer[][]{
                {5, 0}, {50, 999}
        };
    }
}
