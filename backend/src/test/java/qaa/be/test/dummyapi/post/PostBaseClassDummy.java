package qaa.be.test.dummyapi.post;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.test.dummyapi.BaseDummyApi;

@Slf4j
public abstract class PostBaseClassDummy extends BaseDummyApi {

    protected UserModel user;

    @BeforeClass(alwaysRun = true)
    protected void createUser() {
        log.info("New user is created.");
        user = restWrapper.usingUsers().createItem(UserModel.generateRandomUser());
    }
}
