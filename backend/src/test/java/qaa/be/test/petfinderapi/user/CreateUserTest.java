package qaa.be.test.petfinderapi.user;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.error.DetailErrorModel;
import qaa.be.petfinderapi.models.error.TraceErrorModel;
import qaa.be.petfinderapi.models.user.PetUser;
import qaa.be.petfinderapi.util.ErrorTypePet;
import qaa.be.test.petfinderapi.BasePetFinderApi;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static qaa.be.petfinderapi.models.user.PetUser.DOMAIN;

@Slf4j
public class CreateUserTest extends BasePetFinderApi {
    @Test
    public void createUser() {
        PetUser createUser = PetUser.generateRandomUser();
        PetUser responseCreate = restWrapper.usingUsers().createItem(createUser);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate user is created successfully!");
        assertEquals(responseCreate.getFirstName(), createUser.getFirstName());
        assertEquals(responseCreate.getLastName(), createUser.getLastName());
        assertEquals(responseCreate.getEmail(), createUser.getEmail());
        assertEquals(responseCreate.getPhoneNumber(), createUser.getPhoneNumber());
    }

    @Test
    public void createLogin() {
        PetUser createUser = PetUser.generateRandomUser();
        restWrapper.usingUsers().createItem(createUser);

        PetUser createLogin = new PetUser();
        createLogin.setEmail(createUser.getEmail());
        createLogin.setPassword(createUser.getPassword());
        PetUser responseCreate = restWrapper.usingUsers().createLogin(createLogin);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate login is successful!");
        assertEquals(responseCreate.getEmail(), createLogin.getEmail());
    }

    @DataProvider(name = "invalidFirstAndLastName")
    public Object[][] createInvalidInputsForFirstAndLastName() {
        return new Object[][]{
                {randomAlphanumeric(257), randomAlphanumeric(3), ErrorTypePet.FIRST_NAME_LENGTH},
                {"", randomAlphanumeric(3), ErrorTypePet.FIRST_NAME_TOO_SHORT},
                {" ", randomAlphanumeric(3), ErrorTypePet.FIRST_NAME_REQUIRED_DETAIL},
                {"5", randomAlphanumeric(3), ErrorTypePet.FIRST_NAME_LETTERS},
                {"%", randomAlphanumeric(3), ErrorTypePet.FIRST_NAME_LETTERS},
                {XSS_INJECTION, randomAlphanumeric(3), ErrorTypePet.FIRST_NAME_NOT_VALID},
                {SQL_INJECTION, randomAlphanumeric(3), ErrorTypePet.FIRST_NAME_NOT_VALID},
                {randomAlphanumeric(3), randomAlphanumeric(257), ErrorTypePet.LAST_NAME_LENGTH},
                {randomAlphanumeric(3), "", ErrorTypePet.LAST_NAME_TOO_SHORT},
                {randomAlphanumeric(3), " ", ErrorTypePet.LAST_NAME_REQUIRED_DETAIL},
                {randomAlphanumeric(3), "5", ErrorTypePet.LAST_NAME_LETTERS},
                {randomAlphanumeric(3), "%", ErrorTypePet.LAST_NAME_LETTERS},
                {randomAlphanumeric(3), XSS_INJECTION, ErrorTypePet.LAST_NAME_NOT_VALID},
                {randomAlphanumeric(3), SQL_INJECTION, ErrorTypePet.LAST_NAME_NOT_VALID},
        };
    }

    @Test(dataProvider = "invalidFirstAndLastName")
    public void createUserWithInvalidFirstName(String firstName, String lastName, String error) {
        PetUser createInvalidUser = PetUser.generateRandomUser();
        createInvalidUser.setFirstName(firstName);
        createInvalidUser.setLastName(lastName);

        restWrapper.setErrorField("Detail");
        restWrapper.usingUsers().createItem(createInvalidUser);
        DetailErrorModel responseCreate = restWrapper.processLastError(DetailErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getDetail().trim(), error);
    }

    @Test
    public void createUserWithoutFirstName() {
        PetUser createInvalidUser = new PetUser();
        createInvalidUser.setLastName(randomAlphanumeric(4));
        createInvalidUser.setEmail(randomAlphanumeric(4).toLowerCase() + DOMAIN);
        createInvalidUser.setPassword(randomAlphabetic(4).toLowerCase() + "A5*");
        createInvalidUser.setPhoneNumber(randomNumeric(5));

        restWrapper.usingUsers().createItem(createInvalidUser);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getFirstName().toString(), ErrorTypePet.FIRST_NAME_REQUIRED_ERROR);
    }

    @Test
    public void createUserWithoutLastName() {
        PetUser createInvalidUser = new PetUser();
        createInvalidUser.setFirstName(randomAlphanumeric(4));
        createInvalidUser.setEmail(randomAlphanumeric(4).toLowerCase() + DOMAIN);
        createInvalidUser.setPassword(randomAlphabetic(4).toLowerCase() + "A5*");
        createInvalidUser.setPhoneNumber(randomNumeric(5));

        restWrapper.usingUsers().createItem(createInvalidUser);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getLastName().toString(), ErrorTypePet.LAST_NAME_REQUIRED_ERROR);
    }

    @DataProvider(name = "invalidEmail")
    public Object[][] createInvalidInputsForEmail() {
        return new Object[][]{
                {"", ErrorTypePet.EMAIL_TOO_SHORT},
                {randomAlphanumeric(91).toLowerCase() + DOMAIN, ErrorTypePet.EMAIL_LENGTH},
                {" ", ErrorTypePet.EMAIL_REQUIRED_DETAIL},
                {"5", ErrorTypePet.EMAIL_NOT_VALID},
                {"@mail.com", ErrorTypePet.EMAIL_NOT_VALID},
                {"d@", ErrorTypePet.EMAIL_NOT_VALID},
                {SQL_INJECTION, ErrorTypePet.EMAIL_NOT_VALID},
                {XSS_INJECTION, ErrorTypePet.EMAIL_NOT_VALID},
                {randomAlphanumeric(3) + "@s@s.com", ErrorTypePet.EMAIL_NOT_VALID},
                {randomAlphanumeric(3) + "@d", ErrorTypePet.EMAIL_NOT_VALID},
                {randomAlphanumeric(3) + "@.com", ErrorTypePet.EMAIL_NOT_VALID},
                {randomAlphanumeric(3) + "@.s.d.l.com", ErrorTypePet.EMAIL_NOT_VALID},
                {randomAlphanumeric(3) + "@s,com", ErrorTypePet.EMAIL_NOT_VALID},
        };
    }

    @Test(dataProvider = "invalidEmail")
    public void createUserWithInvalidEmail(String email, String error) {
        PetUser createInvalidUser = PetUser.generateRandomUser();
        createInvalidUser.setEmail(email);

        restWrapper.setErrorField("Detail");
        restWrapper.usingUsers().createItem(createInvalidUser);
        DetailErrorModel responseCreate = restWrapper.processLastError(DetailErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getDetail().trim(), error);
    }

    @Test
    public void createUserWithoutEmail() {
        PetUser createInvalidUser = new PetUser();
        createInvalidUser.setFirstName(randomAlphanumeric(4));
        createInvalidUser.setLastName(randomAlphanumeric(4));
        createInvalidUser.setPassword(randomAlphabetic(4).toLowerCase() + "A5*");
        createInvalidUser.setPhoneNumber(randomNumeric(5));

        restWrapper.usingUsers().createItem(createInvalidUser);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getEmail().toString(), ErrorTypePet.EMAIL_REQUIRED_ERROR);
    }

    @Test
    public void createUserWithAlreadyRegisteredEmail() {
        PetUser createUser = PetUser.generateRandomUser();
        restWrapper.usingUsers().createItem(createUser);

        PetUser createSecondUser = PetUser.generateRandomUser();
        createSecondUser.setEmail(createUser.getEmail());
        restWrapper.setErrorField("Detail");
        restWrapper.usingUsers().createItem(createSecondUser);
        DetailErrorModel responseCreate = restWrapper.processLastError(DetailErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertTrue(responseCreate.getDetail().contains(ErrorTypePet.EMAIL_DUPLICATE));
    }

    @Test
    public void createUserWithAlreadyRegisteredEmailWithSpaces() {
        PetUser createUser = PetUser.generateRandomUser();
        restWrapper.usingUsers().createItem(createUser);

        PetUser createSecondUser = PetUser.generateRandomUser();
        createSecondUser.setEmail(" " + createUser.getEmail());
        restWrapper.setErrorField("Detail");
        restWrapper.usingUsers().createItem(createSecondUser);
        DetailErrorModel responseCreate = restWrapper.processLastError(DetailErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertTrue(responseCreate.getDetail().contains(ErrorTypePet.EMAIL_DUPLICATE));
    }

    @DataProvider(name = "invalidPassword")
    public Object[][] createInvalidInputsForPassword() {
        return new Object[][]{
                {"", ErrorTypePet.PASSWORD_REQUIRED},
                {randomAlphanumeric(28).toLowerCase() + "A1*", ErrorTypePet.PASSWORD_LENGTH},
                {" ", ErrorTypePet.PASSWORD_REQUIRED},
                {randomAlphanumeric(5).toLowerCase() + "A5", ErrorTypePet.PASSWORD_NOT_VALID}
        };
    }

    @Test(dataProvider = "invalidPassword")
    public void createUserWithInvalidPassword(String password, String error) {
        PetUser createInvalidUser = PetUser.generateRandomUser();
        createInvalidUser.setPassword(password);

        restWrapper.setErrorField("Detail");
        restWrapper.usingUsers().createItem(createInvalidUser);
        DetailErrorModel responseCreate = restWrapper.processLastError(DetailErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getDetail().trim(), error);
    }

    @Test
    public void createUserWithoutPassword() {
        PetUser createInvalidUser = new PetUser();
        createInvalidUser.setEmail(randomAlphanumeric(4).toLowerCase() + DOMAIN);
        createInvalidUser.setFirstName(randomAlphanumeric(4));
        createInvalidUser.setLastName(randomAlphanumeric(4));
        createInvalidUser.setPhoneNumber(randomNumeric(5));

        restWrapper.usingUsers().createItem(createInvalidUser);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getPassword().toString(), ErrorTypePet.PASSWORD_REQUIRED_ERROR);
    }

    @DataProvider(name = "invalidPhone")
    public Object[][] createInvalidInputsForPhone() {
        return new Object[][]{
                {"", ErrorTypePet.PHONE_NUMBER_TOO_SHORT},
                {randomNumeric(16), ErrorTypePet.PHONE_LENGTH},
                {" ", ErrorTypePet.PHONE_REQUIRED_DETAIL},
                {randomAlphanumeric(5), ErrorTypePet.PHONE_NOT_VALID},
                {"***", ErrorTypePet.PHONE_NOT_VALID}
        };
    }

    @Test(dataProvider = "invalidPhone")
    public void createUserWithInvalidPhone(String phone, String error) {
        PetUser createInvalidUser = PetUser.generateRandomUser();
        createInvalidUser.setPhoneNumber(phone);

        restWrapper.setErrorField("Detail");
        restWrapper.usingUsers().createItem(createInvalidUser);
        DetailErrorModel responseCreate = restWrapper.processLastError(DetailErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getDetail().trim(), error);
    }

    @Test
    public void createUserWithoutPhone() {
        PetUser createInvalidUser = new PetUser();
        createInvalidUser.setEmail(randomAlphanumeric(4).toLowerCase() + DOMAIN);
        createInvalidUser.setFirstName(randomAlphanumeric(4));
        createInvalidUser.setLastName(randomAlphanumeric(4));
        createInvalidUser.setPassword(randomAlphabetic(4).toLowerCase() + "A5*");

        restWrapper.usingUsers().createItem(createInvalidUser);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getPhoneNumber().toString(), ErrorTypePet.PHONE_REQUIRED_ERROR);
    }

    @Test
    public void createUserWithoutMandatoryFields() {
        PetUser createInvalidUser = new PetUser();

        restWrapper.usingUsers().createItem(createInvalidUser);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getEmail().toString(), ErrorTypePet.EMAIL_REQUIRED_ERROR);
        assertEquals(responseCreate.getErrors().getFirstName().toString(), ErrorTypePet.FIRST_NAME_REQUIRED_ERROR);
        assertEquals(responseCreate.getErrors().getLastName().toString(), ErrorTypePet.LAST_NAME_REQUIRED_ERROR);
        assertEquals(responseCreate.getErrors().getPassword().toString(), ErrorTypePet.PASSWORD_REQUIRED_ERROR);
        assertEquals(responseCreate.getErrors().getPhoneNumber().toString(), ErrorTypePet.PHONE_REQUIRED_ERROR);
    }

    @Test
    public void loginWithoutEmail() {
        PetUser createUser = PetUser.generateRandomUser();

        restWrapper.usingUsers().createItem(createUser);

        PetUser createLogin = new PetUser();
        createLogin.setPassword(createUser.getPassword());
        restWrapper.usingUsers().createLogin(createLogin);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getEmail().toString(), ErrorTypePet.EMAIL_REQUIRED_ERROR);
    }

    @Test
    public void loginWithoutPassword() {
        PetUser createUser = PetUser.generateRandomUser();

        restWrapper.usingUsers().createItem(createUser);

        PetUser createLogin = new PetUser();
        createLogin.setEmail(createUser.getEmail());
        restWrapper.usingUsers().createLogin(createLogin);
        TraceErrorModel responseCreate = restWrapper.processLastError(TraceErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getErrors().getPassword().toString(), ErrorTypePet.PASSWORD_REQUIRED_ERROR);
    }

    @DataProvider(name = "invalidInputs")
    public Object[][] createInvalidInputs() {
        return new Object[][]{
                {"", randomAlphanumeric(3), ErrorTypePet.EMAIL_TOO_SHORT},
                {"string@.com", "", ErrorTypePet.PASSWORD_REQUESTED_DETAIL},
                {"ss", "", ErrorTypePet.EMAIL_NOT_VALID + " " + ErrorTypePet.PASSWORD_REQUESTED_DETAIL}
        };
    }

    @Test(dataProvider = "invalidInputs")
    public void loginWithInvalidInputs(String email, String password, String error) {

        PetUser createLogin = new PetUser();
        createLogin.setEmail(email);
        createLogin.setPassword(password);

        restWrapper.setErrorField("Detail");
        restWrapper.usingUsers().createLogin(createLogin);
        DetailErrorModel responseCreate = restWrapper.processLastError(DetailErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);
        log.info("Validate user isn't created!");
        assertEquals(responseCreate.getDetail().trim(), error);
    }
}
