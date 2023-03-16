package qaa.be.test.dummyapi.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.UserErrorModel;
import qaa.be.dummyapi.models.user.Location;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.test.dummyapi.BaseDummyApi;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.testng.Assert.assertEquals;

public class UpdateUserTest extends BaseDummyApi {

    @DataProvider(name = "update_valid_first_and_last_name_data")
    public static Object[][] updateValidFirstAndLastName() {
        UserModel firstAndLastNameMin = UserModel.generateRandomUser();
        firstAndLastNameMin.setFirstName(randomAlphabetic(2));
        firstAndLastNameMin.setLastName(randomAlphabetic(2));

        UserModel firstAndLastNameMax = UserModel.generateRandomUser();
        firstAndLastNameMax.setFirstName(randomAlphabetic(30));
        firstAndLastNameMax.setLastName(randomAlphabetic(30));

        return new Object[][]{
                {firstAndLastNameMin},
                {firstAndLastNameMax},
        };
    }

    @Test(dataProvider = "update_valid_first_and_last_name_data",
            groups = {"user_test"})
    void testUpdateValidFirstNameAndLastName(UserModel data) {
        UserModel newUserForUpdate = UserModel.generateRandomUser();
        String id = createUser(newUserForUpdate);
        UserModel response = restWrapper.usingUsers().updateItem(id, data);

        softAssert.assertTrue(restWrapper.getStatusCode() == SC_OK);
        softAssert.assertEquals(response.getFirstName(), data.getFirstName());
        softAssert.assertEquals(response.getLastName(), data.getLastName());
        softAssert.assertEquals(response.getId(), id);
        softAssert.assertEquals(response.getEmail(), newUserForUpdate.getEmail());
        softAssert.assertEquals(response.getGender(), "male");
        softAssert.assertEquals(response.getTitle(), "mr");
        softAssert.assertTrue(response.getUpdatedDate().contains(
                OffsetDateTime.now(ZoneOffset.UTC).format
                        (DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"))));
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testUpdateValidDataInAllFields() {
        UserModel dataBody = UserModel.generateRandomAllFieldsUser();
        UserModel newUserForUpdate = UserModel.generateRandomUser();
        newUserForUpdate.setEmail(dataBody.getEmail());
        String id = createUser(newUserForUpdate);
        OffsetDateTime beforeRequest = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS).minusSeconds(2);
        UserModel response = restWrapper.usingUsers().updateItem(id, dataBody);
        OffsetDateTime afterRequest = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS).plusSeconds(4);
        softAssert.assertTrue(restWrapper.getStatusCode() == SC_OK);
        softAssert.assertEquals(response.getFirstName(), dataBody.getFirstName());
        softAssert.assertEquals(response.getLastName(), dataBody.getLastName());
        softAssert.assertEquals(response.getId(), id);
        softAssert.assertEquals(response.getEmail(), newUserForUpdate.getEmail());
        softAssert.assertEquals(response.getGender(), dataBody.getGender());
        softAssert.assertEquals(response.getTitle(), dataBody.getTitle());
        softAssert.assertEquals(response.getPhone(), dataBody.getPhone());
        softAssert.assertEquals(response.getPicture(), dataBody.getPicture());
        softAssert.assertEquals(response.getDateOfBirth(), dataBody.getDateOfBirth());
        softAssert.assertTrue(checkDateIsBetween(response.getRegisterDate(), beforeRequest, afterRequest));
        softAssert.assertEquals(response.getLocation(), dataBody.getLocation());
        softAssert.assertAll();
    }

    @DataProvider(name = "update_all_fields_invalid_data")
    public static Object[][] updateAllFieldsInvalidData() {
        UserModel allFieldsLessThanMin = new UserModel();
        allFieldsLessThanMin.setFirstName(RandomStringUtils.randomAlphabetic(1));
        allFieldsLessThanMin.setLastName(RandomStringUtils.randomAlphabetic(1));
        allFieldsLessThanMin.setPhone(RandomStringUtils.randomNumeric(4));
        allFieldsLessThanMin.setPicture(RandomStringUtils.randomAlphanumeric(10));
        allFieldsLessThanMin.setTitle("human");
        allFieldsLessThanMin.setGender("human");
        allFieldsLessThanMin.setDateOfBirth("1899/12/31");
        Location locationMin = new Location(
                RandomStringUtils.randomAlphabetic(4),
                RandomStringUtils.randomAlphabetic(1),
                RandomStringUtils.randomAlphabetic(1),
                RandomStringUtils.randomAlphabetic(1),
                "-13:00"
        );
        allFieldsLessThanMin.setLocation(locationMin);

        UserModel allFieldsMoreThanMax = new UserModel();
        allFieldsMoreThanMax.setFirstName(RandomStringUtils.randomAlphabetic(31));
        allFieldsMoreThanMax.setLastName(RandomStringUtils.randomAlphabetic(31));
        allFieldsMoreThanMax.setEmail(RandomStringUtils.randomAlphabetic(7));
        allFieldsMoreThanMax.setPhone(RandomStringUtils.randomNumeric(21));
        allFieldsMoreThanMax.setPicture(RandomStringUtils.randomAlphanumeric(10));
        allFieldsMoreThanMax.setDateOfBirth(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1).
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")));
        allFieldsMoreThanMax.setTitle("human");
        allFieldsMoreThanMax.setGender("human");
        Location locationMoreThanMax = new Location(
                RandomStringUtils.randomAlphabetic(101),
                RandomStringUtils.randomAlphabetic(31),
                RandomStringUtils.randomAlphabetic(31),
                RandomStringUtils.randomAlphabetic(31),
                "+15:00"
        );
        allFieldsMoreThanMax.setLocation(locationMoreThanMax);

        return new UserModel[][]{
                {allFieldsLessThanMin},
                {allFieldsMoreThanMax},
        };
    }

    @Test(dataProvider = "update_all_fields_invalid_data", groups = {"user_test"})
    @Bug(id = "", description = "Update a user with invalid data should return a response error but instead return a " +
            "response status code 200")
    void testUpdateInvalidDataInAllFields(UserModel dataBody) {
        String id = createUser(UserModel.generateRandomUser());
        restWrapper.usingUsers().updateItem(id, dataBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        // TODO I expect an ErrorModel rsp but received UserModel rsp with status code 200
        // TODO After bug will be fixed need to check the response
    }

    @Test(groups = {"user_test"})
    @Bug(id = "", description = "Update email return a status code 200")
    void testUpdateEmail() {
        UserModel userForUpdate = UserModel.generateRandomUser();
        String id = createUser(userForUpdate);
        userForUpdate.setEmail(randomAlphabetic(5) + UserModel.DOMAIN);
        restWrapper.usingUsers().updateItem(id, userForUpdate);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        // TODO check response when bug will be fixed, now status code 200 and response UserModel type
    }

    @Test(groups = {"user_test"})
    @Bug(id = "", description = "Update id return response status code 200")
    void testTryUpdateId() {
        UserModel userForUpdate = UserModel.generateRandomUser();
        String id = createUser(userForUpdate);
        userForUpdate.setId(randomAlphabetic(24));

        UserModel response = restWrapper.usingUsers().updateItem(id, userForUpdate);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertNotEquals(response.getId(), userForUpdate.getId());
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    @Bug(id = "", description = "Update first and last name with script don't return any error. Status code 200")
    void testUpdateWithXssInjection() {
        UserModel userWithXss = new UserModel();
        userWithXss.setFirstName(XSS_INJECTION);
        userWithXss.setLastName(XSS_INJECTION);
        String id = createUser(UserModel.generateRandomUser());
        restWrapper.usingUsers().updateItem(id, userWithXss);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        //TODO check response when bug will be fixed
    }

    @Test(groups = {"user_test"})
    void testUpdateWithoutAppId() {
        String id = createUser(UserModel.generateRandomUser());
        restWrapperWithoutAuth.usingUsers().
                updateItem(id, UserModel.generateRandomUser());
        UserErrorModel errorRsp = restWrapperWithoutAuth.processLastError(UserErrorModel.class);

        assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);
        assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_MISSING_APP_ID);
    }
}
