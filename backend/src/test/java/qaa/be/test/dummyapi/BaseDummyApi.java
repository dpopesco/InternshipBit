package qaa.be.test.dummyapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;
import qaa.be.dummyapi.DummyApiWrapper;
import qaa.be.dummyapi.models.ResponseStructure;
import qaa.be.dummyapi.models.comment.CommentsCollection;
import qaa.be.dummyapi.models.post.PostsCollection;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.dummyapi.models.user.UsersCollection;
import qaa.be.util.Properties;
import qaa.be.util.TestContext;
import qaa.internship.util.PlainTextReporter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.assertEquals;


@Slf4j
@ContextConfiguration(classes = TestContext.class)
@Listeners(PlainTextReporter.class)
public abstract class BaseDummyApi extends AbstractTestNGSpringContextTests {

    protected static final int LIMIT_DEFAULT_VALUE = 20;
    protected static final int PAGE_DEFAULT_VALUE = 0;
    protected static final String CREATED_ITEMS_PARAMS = "created=1";
    protected static final String XSS_INJECTION = "<script>alert(\"XSS\")</script>";

    @Autowired
    protected DummyApiWrapper restWrapper;

    @Autowired
    protected DummyApiWrapper restWrapperWithoutAuth;

    @Autowired
    protected Properties properties;

    public SoftAssert softAssert;

    @BeforeClass()
    public void setUpMethod() {
        softAssert = new SoftAssert();
        log.info("Entering: " + this.getClass().toString());
        log.info("Setting in header app-id!");
        restWrapper.addRequestHeader("app-id", properties.getAPP_ID());
    }

    @AfterSuite(alwaysRun = true)
    protected void tearDown() {
        log.info("*************************Tear Down Start************************");

        log.info("Created users are deleted");
        UsersCollection usersCollectionRsp = restWrapper.usingUsers().usingParams(CREATED_ITEMS_PARAMS).getItems();
        usersCollectionRsp.getData().forEach(user -> restWrapper.usingUsers().deleteItem(user.getId()));

        log.info("Created posts are deleted.");
        PostsCollection postsCollection = restWrapper.usingPosts().usingParams(CREATED_ITEMS_PARAMS).getItems();
        postsCollection.getData().stream().forEach(x -> restWrapper.usingPosts().deleteItem(x.getId()));

        log.info("Created comments are deleted.");
        CommentsCollection commentsCollection = restWrapper.usingComments().usingParams(CREATED_ITEMS_PARAMS).getItems();
        commentsCollection.getData().stream().forEach(x -> restWrapper.usingComments().deleteItem(x.getId()));

        log.info("*************************Tear Down Finish************************");
    }

    /**
     * This method verify numbers of elements from data field
     *
     * @param response user ResponseStructure type because need to allow both UsersCollection and PostsCollection type
     */
    public void verifyResponseDataSize(ResponseStructure response) {
        // add + 1 because page start from 0
        int pageNumber = response.getPage() + 1;
        if (pageNumber <= response.getTotal() / response.getLimit()) {
            assertEquals(response.getData().size(), response.getLimit());
        } else if (pageNumber == response.getTotal() / response.getLimit() + 1) {
            assertEquals(response.getData().size(), response.getTotal() % response.getLimit());
        } else {
            assertEquals(response.getData().size(), 0);
        }
    }

    public String createUser(UserModel userData) {
        UserModel user = restWrapper.usingUsers().createItem(userData);
        return user.getId();
    }

    protected boolean checkDateIsBetween(String dateToCheck, OffsetDateTime before, OffsetDateTime after) {
        OffsetDateTime dateFormatted = OffsetDateTime.parse(dateToCheck,
                DateTimeFormatter.ISO_DATE_TIME);
        return (dateFormatted.isAfter(before) && dateFormatted.isBefore(after));
    }
}
