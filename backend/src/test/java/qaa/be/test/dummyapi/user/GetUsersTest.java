package qaa.be.test.dummyapi.user;

import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.UserErrorModel;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.dummyapi.models.user.UsersCollection;
import qaa.be.test.dummyapi.BaseDummyApi;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;
import qaa.be.dummyapi.util.PaginationDataProvider;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;

public class GetUsersTest extends BaseDummyApi {

    @Test(groups = {"user_test"})
    void testGetUsersListWithValidAppId() {
        UsersCollection response = restWrapper.usingUsers().getItems();

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertEquals(response.getData().size(), LIMIT_DEFAULT_VALUE);
        softAssert.assertEquals(response.getLimit(), LIMIT_DEFAULT_VALUE);
        softAssert.assertEquals(response.getPage(), PAGE_DEFAULT_VALUE);
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testGetUsersWithInvalidAppId() {
        restWrapperWithoutAuth.usingUsers().getItems();
        restWrapperWithoutAuth.processLastError(UserErrorModel.class).hasError(ErrorType.ERROR_MSG_MISSING_APP_ID);

        softAssert.assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);
        softAssert.assertAll();
    }

    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "valid_limit_values", groups = {"user_test"})
    void testGetUsersWithValidLimitParam(int limitValue) {
        UsersCollection response = restWrapper.usingUsers().usingParams("limit=" + limitValue).getItems();

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertEquals(response.getData().size(), limitValue);
        softAssert.assertEquals(response.getLimit(), limitValue);
        softAssert.assertEquals(response.getPage(), PAGE_DEFAULT_VALUE);
        softAssert.assertAll();
    }

    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "invalid_limit_values", groups = {"user_test"})
    @Bug(id = "", description = "Limit params accept invalid values. Response status code 200")
    void testGetUsersWithInvalidLimitParam(Object limitValue) {
        restWrapper.usingUsers().usingParams(
                "limit=" + limitValue).getItems();
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
    }

    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "valid_page_values", groups = {"user_test"})
    void testGetUsersWithValidPageParam(int pageValue) {
        UsersCollection response = restWrapper.usingUsers().usingParams(
                "page=" + pageValue).getItems();

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertEquals(response.getPage(), pageValue);
        verifyResponseDataSize(response);
        softAssert.assertAll();
    }

    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "invalid_page_values", groups = {"user_test"})
    @Bug(id = "", description = "Page params accept invalid values. Status code 200")
    void testGetUsersWithInvalidPageParam(Object pageValue) {
        restWrapper.usingUsers().usingParams(
                "page=" + pageValue).getItems();
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
    }

    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "valid_limit_and_page_values", groups = {"user_test"})
    void testGetUsersWithValidPageAndLimitParameters(int limitValue, int pageValue) {
        UsersCollection response = restWrapper.usingUsers().usingParams("limit=" + limitValue,
                "page=" + pageValue).getItems();

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertEquals(response.getLimit(), limitValue);
        softAssert.assertEquals(response.getPage(), pageValue);
        verifyResponseDataSize(response);
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testGetUserWithValidId() {
        UserModel newUser = UserModel.generateRandomUser();
        String newUserId = createUser(newUser);
        UserModel response = restWrapper.usingUsers().getItem(newUserId);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertEquals(response.getId(), newUserId);
        softAssert.assertEquals(response.getFirstName(), newUser.getFirstName());
        softAssert.assertEquals(response.getLastName(), newUser.getLastName());
        softAssert.assertAll();
    }

    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "invalid_ids", groups = {"user_test"})
    @Bug(id = "", description = "24 length id should return  error message Resources not found / " +
            "empty string id should return error message")
    void testGetUserWithInvalidId(String id) {
        restWrapper.usingUsers().getItem(id);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        if (id.length() == 24) {
            softAssert.assertEquals(restWrapper.getStatusCode(), SC_NOT_FOUND);
            softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_RSC_NOT_FOUND);
        } else {
            softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
            softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
        }
        softAssert.assertAll();
    }

    @Test
    void testGetAUserThatWasDeleted() {
        String newUseId = createUser(UserModel.generateRandomUser());
        restWrapper.usingUsers().deleteItem(newUseId);
        restWrapper.usingUsers().getItem(newUseId);
        restWrapper.processLastError(UserErrorModel.class).hasError(ErrorType.ERROR_MSG_RSC_NOT_FOUND);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_NOT_FOUND);
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testGetCreatedUsers() {
        String id1 = createUser(UserModel.generateRandomUser());
        String id2 = createUser(UserModel.generateRandomUser());
        UsersCollection response = restWrapper.usingUsers().usingParams(CREATED_ITEMS_PARAMS).getItems();
        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        long usersCreated = response.getData()
                .stream()
                .filter(userCreated -> userCreated.getId().equals(id1) || userCreated.getId().equals(id2))
                .count();
        softAssert.assertEquals(usersCreated, 2);
        softAssert.assertAll();
    }
}
