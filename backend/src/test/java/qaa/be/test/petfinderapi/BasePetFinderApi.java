package qaa.be.test.petfinderapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import qaa.be.petfinderapi.PetFinderWrapper;
import qaa.be.petfinderapi.models.user.PetUser;
import qaa.be.util.Properties;
import qaa.be.util.TestContext;
import qaa.internship.util.PlainTextReporter;

@Slf4j
@ContextConfiguration(classes = TestContext.class)
@Listeners(PlainTextReporter.class)
public abstract class BasePetFinderApi extends AbstractTestNGSpringContextTests {

    @Autowired
    protected PetFinderWrapper restWrapper;

    @Autowired
    protected PetFinderWrapper restWrapperWithoutToken;

    @Autowired
    protected Properties properties;
    protected String userId;
    protected String token;

    protected static final String XSS_INJECTION = "<script>alert(\"XSS\")</script>";
    protected static final String SQL_INJECTION = "user ' OR' 5'='5";

    @BeforeClass(alwaysRun = true)
    public void addHeader() {
        log.info("Entering: " + this.getClass().toString());
        log.info("Setting in header access token");
        loginWithRandomUser();
        restWrapper.addRequestHeader("Authorization", "Bearer " + token);
    }

    @BeforeMethod
    public void setError() {
        restWrapper.setErrorField("errors");
    }

    public PetUser loginWithRandomUser() {
        PetUser createUser = PetUser.generateRandomUser();
        restWrapper.usingUsers().createItem(createUser);

        PetUser createLogin = new PetUser();
        createLogin.setEmail(createUser.getEmail());
        createLogin.setPassword(createUser.getPassword());
        PetUser responseCreate = restWrapper.usingUsers().createLogin(createLogin);
        this.userId = responseCreate.getId();
        this.token = responseCreate.getToken();
        return responseCreate;
    }
}
