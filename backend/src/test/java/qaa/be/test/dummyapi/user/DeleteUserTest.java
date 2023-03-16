package qaa.be.test.dummyapi.user;

import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.UserErrorModel;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.dummyapi.util.ErrorType;
import qaa.be.test.dummyapi.BaseDummyApi;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;

public class DeleteUserTest extends BaseDummyApi {
    @Test(groups = {"user_test"})
    void testDeleteUser() {
        String id = createUser(UserModel.generateRandomUser());
        UserModel response = restWrapper.usingUsers().deleteItem(id);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertEquals(response.getId(), id);
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testDeleteAnAlreadyDeletedUser() {
        String id = createUser(UserModel.generateRandomUser());
        restWrapper.usingUsers().deleteItem(id);
        restWrapper.usingUsers().deleteItem(id);
        restWrapper.processLastError(UserErrorModel.class).hasError(ErrorType.ERROR_MSG_RSC_NOT_FOUND);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_NOT_FOUND);
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testDeleteUserWithoutAppId() {
        String id = createUser(UserModel.generateRandomUser());
        restWrapperWithoutAuth.usingUsers().deleteItem(id);
        restWrapperWithoutAuth.processLastError(UserErrorModel.class).hasError(ErrorType.ERROR_MSG_MISSING_APP_ID);

        softAssert.assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testDeleteInvalidUserId() {
        restWrapper.usingUsers().deleteItem(randomAlphanumeric(24));
        restWrapper.processLastError(UserErrorModel.class).hasError(ErrorType.ERROR_MSG_PARAMS_NOT_VALID);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertAll();
    }

}