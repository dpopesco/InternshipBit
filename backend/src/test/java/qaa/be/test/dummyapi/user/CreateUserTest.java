package qaa.be.test.dummyapi.user;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import qaa.be.dummyapi.enums.Gender;
import qaa.be.dummyapi.enums.Title;
import qaa.be.dummyapi.models.error.UserErrorModel;
import qaa.be.dummyapi.models.user.Location;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.dummyapi.util.ErrorType;
import qaa.be.test.dummyapi.BaseDummyApi;
import qaa.internship.util.Bug;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;

public class CreateUserTest extends BaseDummyApi {

    @DataProvider(name = "user_valid_mandatory_fields_data")
    public static Object[][] createValidDataForUser() {
        UserModel minFirstAndLastName = UserModel.generateRandomUser();
        minFirstAndLastName.setFirstName(randomAlphabetic(2));
        minFirstAndLastName.setLastName(randomAlphabetic(2));

        UserModel maxFirstAndLastName = UserModel.generateRandomUser();
        maxFirstAndLastName.setFirstName(randomAlphabetic(30));
        maxFirstAndLastName.setLastName(randomAlphabetic(30));

        return new UserModel[][]{
                {minFirstAndLastName},
                {UserModel.generateRandomUser()},
                {maxFirstAndLastName}
        };
    }

    @Test(dataProvider = "user_valid_mandatory_fields_data", groups = {"user_test"})
    @Bug(id = "", description = "Response status code for create a user is 200, not 201.")
    void testCreateUserWithValidRequiredFields(UserModel userBody) {
        UserModel userRsp = restWrapper.usingUsers().createItem(userBody);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_CREATED);
        softAssert.assertEquals(userRsp.getFirstName(), userBody.getFirstName());
        softAssert.assertEquals(userRsp.getLastName(), userBody.getLastName());
        softAssert.assertEquals(userRsp.getEmail(), userBody.getEmail());
        softAssert.assertAll();
    }

    @DataProvider(name = "user_all_fields_valid_data")
    public Object[][] createValidDataForAllFields() {

        Location locationMinLength = new Location(
                randomAlphanumeric(5),
                randomAlphanumeric(2),
                randomAlphanumeric(2),
                randomAlphanumeric(2),
                "-12:00"
        );
        UserModel minAcceptedTitleMsGenderMale = new UserModel(
                randomAlphabetic(2),
                randomAlphabetic(2),
                randomAlphanumeric(6).toLowerCase() + UserModel.DOMAIN,
                Gender.MALE.getGenderType(),
                Title.MS.getTitleType(),
                randomNumeric(5),
                UserModel.PICTURE_URL,
                "1900-01-01T00:00:00.000Z",
                locationMinLength
        );


        Location locationMaxLength = new Location(
                randomAlphanumeric(100),
                randomAlphabetic(30),
                randomAlphabetic(30),
                randomAlphabetic(30),
                "+14:00"
        );
        UserModel maxAcceptedTitleDrGenderOther = new UserModel(
                randomAlphabetic(30),
                randomAlphabetic(30),
                randomAlphabetic(6).toLowerCase() + UserModel.DOMAIN,
                Gender.OTHER.getGenderType(),
                Title.DR.getTitleType(),
                randomNumeric(20),
                UserModel.PICTURE_URL,
                OffsetDateTime.now(ZoneOffset.UTC).minusDays(1).
                        format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")),
                locationMaxLength
        );

        return new UserModel[][]{
                {minAcceptedTitleMsGenderMale},
                {maxAcceptedTitleDrGenderOther},
        };
    }

    @Test(dataProvider = "user_all_fields_valid_data", groups = {"user_test"})
    void testCreateUserWithValidDataInAllAvailableFields(UserModel userBody) throws InterruptedException {
        OffsetDateTime beforeRequest = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS).minusSeconds(2);
        UserModel userRsp = restWrapper.usingUsers().createItem(userBody);
        OffsetDateTime afterRequest = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS).plusSeconds(4);
        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertEquals(userRsp.getTitle(), userBody.getTitle());
        softAssert.assertEquals(userRsp.getPhone(), userBody.getPhone());
        softAssert.assertEquals(userRsp.getGender(), userBody.getGender());
        softAssert.assertEquals(userRsp.getEmail(), userBody.getEmail());
        softAssert.assertEquals(userRsp.getFirstName(), userBody.getFirstName());
        softAssert.assertEquals(userRsp.getLastName(), userBody.getLastName());
        softAssert.assertEquals(userRsp.getDateOfBirth(), userBody.getDateOfBirth());
        softAssert.assertEquals(userRsp.getPicture(), userBody.getPicture());
        softAssert.assertTrue(checkDateIsBetween(userRsp.getRegisterDate(), beforeRequest, afterRequest));
        softAssert.assertEquals(userRsp.getLocation(), userBody.getLocation());
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    @Bug(id = "", description = "Fields firstName and lastName accept script for XSS injection")
    void testCreateUserXssInjection() {
        UserModel userBody = UserModel.generateRandomUser();
        userBody.setFirstName(XSS_INJECTION);
        userBody.setLastName(XSS_INJECTION);
        restWrapper.usingUsers().createItem(userBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        // TODO Check response when bug is fixed
    }

    @Test(groups = {"user_test"})
    void testCreateUserWithoutAppId() {
        UserModel userBody = UserModel.generateRandomUser();
        restWrapperWithoutAuth.usingUsers().createItem(userBody);
        restWrapperWithoutAuth.processLastError(UserErrorModel.class).hasError(ErrorType.ERROR_MSG_MISSING_APP_ID);

        softAssert.assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);
        softAssert.assertAll();
    }

    @DataProvider(name = "invalid_firstName_data")
    public static Object[][] createInvalidFirstName() {
        UserModel userModelEmptyFirstName = UserModel.generateRandomUser();
        userModelEmptyFirstName.setFirstName(null);

        UserModel oneStringFirstName = UserModel.generateRandomUser();
        oneStringFirstName.setFirstName(randomAlphabetic(1));
        String errorMessageOneCharFirstName = String.format(UserApiConstants.ERROR_LESS_THAN_MIN_FIELD, "firstName",
                oneStringFirstName.getFirstName(),UserApiConstants.MIN_FIRST_OR_LAST_NAME);

        UserModel maxStringFirstName = UserModel.generateRandomUser();
        maxStringFirstName.setFirstName(randomAlphabetic(31));
        String errorMessageMaxStringFirstName =
                String.format(UserApiConstants.ERROR_MORE_THAN_MAX_FIELD, "firstName",
                        maxStringFirstName.getFirstName(), UserApiConstants.MAX_FIRST_OR_LAST_NAME);

        return new Object[][]{
                {userModelEmptyFirstName, String.format(UserApiConstants.ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS, "firstName")},
                {oneStringFirstName, errorMessageOneCharFirstName},
                {maxStringFirstName, errorMessageMaxStringFirstName},
        };
    }

    @Test(dataProvider = "invalid_firstName_data", groups = {"user_test"})
    void testCreateUserWithInvalidFirstName(UserModel userDataBody, String errorMessage) {
        restWrapper.usingUsers().createItem(userDataBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertEquals(errorRsp.getData().getFirstName(), errorMessage);
        softAssert.assertAll();
    }

    @DataProvider(name = "invalid_lastName_data")
    public static Object[][] createInvalidLastName() {
        UserModel userModelEmptyLastName = UserModel.generateRandomUser();
        userModelEmptyLastName.setLastName(null);

        UserModel userModelOneStringLastName = UserModel.generateRandomUser();
        userModelOneStringLastName.setLastName(randomAlphabetic(1));
        String errorMessageOneCharLastName =
                String.format(UserApiConstants.ERROR_LESS_THAN_MIN_FIELD, "lastName",
                        userModelOneStringLastName.getLastName(), UserApiConstants.MIN_FIRST_OR_LAST_NAME);

        UserModel userModelMaxStringLastName = UserModel.generateRandomUser();
        userModelMaxStringLastName.setLastName(randomAlphabetic(31));
        String errorMessageMaxStringLastName =
                String.format(UserApiConstants.ERROR_MORE_THAN_MAX_FIELD, "lastName",
                        userModelMaxStringLastName.getLastName(), UserApiConstants.MAX_FIRST_OR_LAST_NAME);

        return new Object[][]{
                {userModelEmptyLastName, String.format(UserApiConstants.ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS, "lastName")},
                {userModelOneStringLastName, errorMessageOneCharLastName},
                {userModelMaxStringLastName, errorMessageMaxStringLastName},
        };
    }

    @Test(dataProvider = "invalid_lastName_data", groups = {"user_test"})
    void testCreateUserWithInvalidLastName(UserModel userDataBody, String errorMessage) {
        restWrapper.usingUsers().createItem(userDataBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertEquals(errorRsp.getData().getLastName(), errorMessage);
        softAssert.assertAll();
    }

    @DataProvider(name = "invalid_email_data")
    public static Object[][] createInvalidEmail() {
        UserModel userModelEmptyEmail = UserModel.generateRandomUser();
        userModelEmptyEmail.setEmail(null);

        UserModel userModelWrongFormatEmail = UserModel.generateRandomUser();
        userModelWrongFormatEmail.setEmail(randomAlphanumeric(7).toLowerCase());
        String errorMessageWrongFormatEmail = String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_FORMAT_EMAIL,
                userModelWrongFormatEmail.getEmail());

        return new Object[][]{
                {userModelEmptyEmail, String.format(UserApiConstants.ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS, "email")},
                {userModelWrongFormatEmail, errorMessageWrongFormatEmail}
        };
    }

    @Test(dataProvider = "invalid_email_data", groups = {"user_test"})
    void testCreateUserWithInvalidEmail(UserModel userDataBody, String errorMessage) {
        restWrapper.usingUsers().createItem(userDataBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertEquals(errorRsp.getData().getEmail(), errorMessage);
        softAssert.assertAll();
    }

    @DataProvider(name = "invalid_first_last_and_email_data")
    public static Object[][] createInvalidFirstLastNameAndEmail() {
        UserModel emptyRequiredFields = UserModel.generateRandomUser();
        emptyRequiredFields.setFirstName(null);
        emptyRequiredFields.setLastName(null);
        emptyRequiredFields.setEmail(null);

        UserModel minFirstNameLastNameEmptyEmail = UserModel.generateRandomUser();
        minFirstNameLastNameEmptyEmail.setFirstName(randomAlphabetic(1));
        minFirstNameLastNameEmptyEmail.setLastName(randomAlphabetic(1));
        minFirstNameLastNameEmptyEmail.setEmail(null);

        UserModel maxFirstNameLasNameWrongFormatEmail = UserModel.generateRandomUser();
        maxFirstNameLasNameWrongFormatEmail.setFirstName(randomAlphabetic(31));
        maxFirstNameLasNameWrongFormatEmail.setLastName(randomAlphabetic(31));
        maxFirstNameLasNameWrongFormatEmail.setEmail(randomAlphabetic(7).toLowerCase());

        return new Object[][]
                {
                        {
                                emptyRequiredFields,
                                String.format(UserApiConstants.ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS, "firstName"),
                                String.format(UserApiConstants.ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS, "lastName"),
                                String.format(UserApiConstants.ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS, "email")
                        },
                        {
                                minFirstNameLastNameEmptyEmail,
                                String.format(UserApiConstants.ERROR_LESS_THAN_MIN_FIELD, "firstName",
                                        minFirstNameLastNameEmptyEmail.getFirstName(),
                                        UserApiConstants.MIN_FIRST_OR_LAST_NAME),
                                String.format(UserApiConstants.ERROR_LESS_THAN_MIN_FIELD, "lastName",
                                        minFirstNameLastNameEmptyEmail.getLastName(),
                                        UserApiConstants.MIN_FIRST_OR_LAST_NAME),
                                String.format(UserApiConstants.ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS, "email")
                        },
                        {
                                maxFirstNameLasNameWrongFormatEmail,
                                String.format(UserApiConstants.ERROR_MORE_THAN_MAX_FIELD, "firstName",
                                        maxFirstNameLasNameWrongFormatEmail.getFirstName(),
                                        UserApiConstants.MAX_FIRST_OR_LAST_NAME),
                                String.format(UserApiConstants.ERROR_MORE_THAN_MAX_FIELD, "lastName",
                                        maxFirstNameLasNameWrongFormatEmail.getLastName(),
                                        UserApiConstants.MAX_FIRST_OR_LAST_NAME),
                                String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_FORMAT_EMAIL,
                                        maxFirstNameLasNameWrongFormatEmail.getEmail()),
                        }
                };
    }

    @Test(dataProvider = "invalid_first_last_and_email_data", groups = {"user_test"})
    void testCreateUserWithInvalidFirstNameLastNameAndEmail(UserModel userDataBody, String errorMessageFirstName,
                                                            String errorMessageLastName, String errorMessageEmail) {
        restWrapper.usingUsers().createItem(userDataBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertEquals(errorRsp.getData().getFirstName(), errorMessageFirstName);
        softAssert.assertEquals(errorRsp.getData().getLastName(), errorMessageLastName);
        softAssert.assertEquals(errorRsp.getData().getEmail(), errorMessageEmail);
        softAssert.assertAll();
    }


    @Test(groups = {"user_test"})
    void testCreateUserWithInvalidTitle() {
        UserModel userBody = UserModel.generateRandomAllFieldsUser();
        userBody.setTitle(randomAlphabetic(7));
        restWrapper.usingUsers().createItem(userBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertEquals(errorRsp.getData().getTitle(),
                String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_TITLE, userBody.getTitle()));
        softAssert.assertAll();
    }

    @Test(groups = {"user_test"})
    void testCreateUserWithInvalidGender() {
        UserModel userBody = UserModel.generateRandomAllFieldsUser();
        userBody.setGender(randomAlphabetic(7));
        restWrapper.usingUsers().createItem(userBody);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertEquals(errorRsp.getData().getGender(),
                String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_GENDER, userBody.getGender()));
        softAssert.assertAll();
    }

    @DataProvider(name = "user_invalid_dateOfBirth_data")
    public Object[][] createInvalidDateOfBirth() {
        UserModel dateOfBirthLessThanMinAcc = UserModel.generateRandomUser();
        dateOfBirthLessThanMinAcc.setDateOfBirth("1899/12/31");

        UserModel dateOfBirthMoreThanMaxAcc = UserModel.generateRandomUser();
        String dateNowPlus = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1).format(
                DateTimeFormatter.ofPattern("E MMM dd yyyy HH:mm:ss"));
        dateOfBirthMoreThanMaxAcc.setDateOfBirth(dateNowPlus);

        UserModel wrongFormatDateOfBirth = UserModel.generateRandomUser();
        wrongFormatDateOfBirth.setDateOfBirth("2020/13/32");

        UserModel wrongNonLeapYearFormat = UserModel.generateRandomUser();
        wrongNonLeapYearFormat.setDateOfBirth("2021/02/29");

        UserModel randomStringDateOfBirth = UserModel.generateRandomUser();
        randomStringDateOfBirth.setDateOfBirth(randomAlphanumeric(6));

        UserModel emptyStringDateOfBirth = UserModel.generateRandomUser();
        emptyStringDateOfBirth.setDateOfBirth("");

        return new Object[][]{
                {dateOfBirthLessThanMinAcc, UserApiConstants.ERROR_DATA_MESSAGE_TOO_LOW_DATE_OF_BIRTH},
                {dateOfBirthMoreThanMaxAcc, String.format(UserApiConstants.ERROR_DATA_MESSAGE_AFTER_MAX_DATE_OF_BIRTH, dateNowPlus)},
                {wrongFormatDateOfBirth, String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_DATE_OF_BIRTH, "2020/13/32")},
                {wrongNonLeapYearFormat, String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_DATE_OF_BIRTH, "2021/02/29")},
                {randomStringDateOfBirth, String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_DATE_OF_BIRTH,
                        randomStringDateOfBirth.getDateOfBirth())},
                {emptyStringDateOfBirth, String.format(UserApiConstants.ERROR_DATA_MESSAGE_WRONG_DATE_OF_BIRTH, "")}
        };
    }

    @Test(dataProvider = "user_invalid_dateOfBirth_data", groups = {"user_test"})
    @Bug(id = "", description = "No error returned when sent a wrong format date of birth (2020/13/32).//" +
            "No error returned when sent a random string in date of birth field.")
    void testCreateUserWithInvalidDateOfBirthField(UserModel userData, String birthErrorMsg) {
        restWrapper.usingUsers().createItem(userData);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertTrue(errorRsp.getData().getDateOfBirth().contains(
                birthErrorMsg));
        softAssert.assertAll();
    }

    @DataProvider(name = "user_invalid_phone_data")
    public static Object[][] createInvalidPhoneData() {
        UserModel phoneLessThanMin = UserModel.generateRandomUser();
        phoneLessThanMin.setPhone(randomNumeric(4));

        UserModel phoneMoreThanMax = UserModel.generateRandomUser();
        phoneMoreThanMax.setPhone(randomNumeric(21));

        UserModel phoneOnlyAlphabetic = UserModel.generateRandomUser();
        phoneOnlyAlphabetic.setPhone(randomAlphabetic(10));

        return new Object[][]{
                {phoneLessThanMin, String.format(UserApiConstants.ERROR_LESS_THAN_MIN_FIELD, "phone",
                        phoneLessThanMin.getPhone(),
                        UserApiConstants.MIN_PHONE)},
                {phoneMoreThanMax, String.format(UserApiConstants.ERROR_MORE_THAN_MAX_FIELD, "phone",
                        phoneMoreThanMax.getPhone(),
                        UserApiConstants.MAX_PHONE)},
                {phoneOnlyAlphabetic, "Invalid phone number"},
        };
    }

    @Test(dataProvider = "user_invalid_phone_data", groups = {"user_test"})
    @Bug(id = "", description = "Phone field accept only alphabetic characters when should not")
    void testCreateUserWithInvalidPhoneField(UserModel userData, String errorMessage) {
        restWrapper.usingUsers().createItem(userData);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        softAssert.assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
        softAssert.assertEquals(errorRsp.getData().getPhone(),
                errorMessage);
        softAssert.assertAll();
    }

    @DataProvider(name = "user_invalid_location_data")
    public static Object[][] createInvalidLocationData() {
        UserModel locationFieldLessThanMinAccepted = UserModel.generateRandomUser();
        Location locationInvalidLessThanMinAcc = new Location(
                randomAlphanumeric(4),
                randomAlphanumeric(1),
                randomAlphanumeric(1),
                randomAlphanumeric(1),
                "-13:00"
        );
        locationFieldLessThanMinAccepted.setLocation(locationInvalidLessThanMinAcc);

        UserModel locationFieldMoreThanMaxAccepted = UserModel.generateRandomUser();
        Location locationMoreThanMaxAc = new Location(
                randomAlphanumeric(101),
                randomAlphanumeric(31),
                randomAlphanumeric(31),
                randomAlphanumeric(31),
                "+15:00"
        );
        locationFieldMoreThanMaxAccepted.setLocation(locationMoreThanMaxAc);

        return new Object[][]{
                {locationFieldLessThanMinAccepted},
                {locationFieldMoreThanMaxAccepted}
        };
    }

    @Test(dataProvider = "user_invalid_location_data", groups = {"user_test"})
    @Bug(id = "", description = "Location fields accept more/less characters than is allowed according to documentation.//" +
            "No error returned")
    void testCreateUserWithInvalidLocationField(UserModel userData) {
        restWrapper.usingUsers().createItem(userData);
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        // TODO  Check the response after the bug is fixed
    }

    @Test(groups = {"user_test"})
    void createUserWithoutBody() {
        restWrapper.usingUsers().createItemWithoutBody();
        UserErrorModel errorRsp = restWrapper.processLastError(UserErrorModel.class);

        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        assertEquals(errorRsp.getError(), ErrorType.ERROR_MSG_BODY);
    }
}
