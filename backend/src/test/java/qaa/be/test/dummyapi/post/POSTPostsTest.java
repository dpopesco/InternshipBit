package qaa.be.test.dummyapi.post;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.models.error.PostErrorModel;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;

@Slf4j
public class POSTPostsTest extends PostBaseClassDummy {
    @Test
    @Bug(id = "", description = "Response status code 200 not 201")
    public void createNewPost() {

        PostPOST createPost = PostPOST.generateRandomPost(user);

        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate post is created successfully!");
        assertEquals(responseCreate.getText(), createPost.getText());
        assertEquals(responseCreate.getImage(), createPost.getImage());
        assertEquals(responseCreate.getLikes(), createPost.getLikes());
        assertEquals(responseCreate.getTags(), createPost.getTags());
        assertEquals(responseCreate.getUser().getId(), createPost.getOwnerId());
    }

    @DataProvider(name = "invalidInputsForText")
    public Object[][] createInvalidData() {
        return new Object[][]{
                {randomAlphanumeric(5)},
                {randomAlphanumeric(1001)}
        };
    }

    @Bug(id = "", description = "bug, api accepts text having (1000 < text field < 6) ")
    @Test(dataProvider = "invalidInputsForText")
    public void createPostWithInvalidDataForText(String text) {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        createPost.setText(text);

        restWrapper.usingPosts().createItem(createPost);
        ErrorModel responseCreate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post is not created as text has more than 1000 characters!");
        log.error("Bug, api accepts text bigger than 1000!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @DataProvider(name = "validInputsForText")
    public Object[][] createValidData() {
        return new Object[][]{
                {randomAlphanumeric(6)},
                {randomAlphanumeric(1000)}
        };
    }

    @Test(dataProvider = "validInputsForText")
    @Bug(id="", description="Response status code is 200 not 201")
    public void createPostWithValidDataForText(String text) {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        createPost.setText(text);

        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate post is created!");
        assertEquals(responseCreate.getText(), createPost.getText());
        assertEquals(responseCreate.getLikes(), createPost.getLikes());
        assertEquals(responseCreate.getTags(), createPost.getTags());
        assertEquals(responseCreate.getUser().getId(), createPost.getOwnerId());
    }

    @Test
    public void createPostWithoutOwnerId() {

        PostPOST createPost = new PostPOST();
        createPost.setText(randomAlphanumeric(4));

        restWrapper.usingPosts().createItem(createPost);
        ErrorModel responseCreate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post cannot be created without ownerId!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    @Bug(id="", description="Response status code is not 201")
    public void createPostOnlyWithOwnerId() {

        PostPOST postCreate = new PostPOST();
        postCreate.setOwnerId(user.getId());

        PostGET responseCreate = restWrapper.usingPosts().createItem(postCreate);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate empty post is created!");
        assertEquals(responseCreate.getText(), "");
        assertEquals(responseCreate.getLikes(), 0);
        assertEquals(responseCreate.getImage(), "");
        assertEquals(responseCreate.getTags().size(), 0);
    }

    @Test
    public void createPostWithSpacesForOwnerId() {

        PostPOST postCreate = new PostPOST();
        postCreate.setOwnerId(" ");

        restWrapper.usingPosts().createItem(postCreate);
        ErrorModel responseCreate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post cannot be created with spaces for ownerId!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    public void createPostWithAlreadyDeletedUser() {

        UserModel createUser = restWrapper.usingUsers().createItem(UserModel.generateRandomUser());
        UserModel deleteUser = restWrapper.usingUsers().deleteItem(createUser.getId());

        PostPOST postCreate = PostPOST.generateRandomPost(deleteUser);

        restWrapper.usingPosts().createItem(postCreate);
        PostErrorModel responseCreate = restWrapper.processLastError(PostErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post cannot be created with deleted user!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    @Bug(id="", description="Api accept XSS script in Text field")
    public void createNewPostUsingXSSForText() {

        PostPOST postCreate = PostPOST.generateRandomPost(user);
        postCreate.setText(XSS_INJECTION);

        PostGET responseCreate = restWrapper.usingPosts().createItem(postCreate);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate post is created when entering xss script for text field!");
        assertEquals(responseCreate.getText(), postCreate.getText());
    }

    @DataProvider(name = "invalidInputsForLikes")
    public Object[][] createInvalidInputsForLikes() {
        return new Object[][]{
                {10.5},
                {-1},
                {randomAlphabetic(3)},
                {Integer.MAX_VALUE + 1}
        };
    }

    @Test(dataProvider = "invalidInputsForLikes")
    @Bug(id="", description="Response status code is 200 not 201")
    public void createPostWithInvalidDataForLikes(Object likes) {

        PostPOST postCreate = PostPOST.generateRandomPost(user);

        JSONObject postS = new JSONObject(postCreate);
        postS.put("likes", likes);

        restWrapper.usingPosts().createItem(postS.toString());
        ErrorModel responseCreate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post cannot be created when sent String, double type and negative figures for number of likes!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @DataProvider(name = "validInputsForLikes")
    public Object[][] createValidInputsForLikes() {
        return new Object[][]{
                {0},
                {Integer.MAX_VALUE}
        };
    }

    @Test(dataProvider = "validInputsForLikes")
    @Bug(id="", description="Response status code is 200 not 201")
    public void createPostWithValidDataForLikes(int likes) {

        PostPOST postCreate = PostPOST.generateRandomPost(user);
        postCreate.setLikes(likes);

        PostGET responseCreate = restWrapper.usingPosts().createItem(postCreate);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate post is created!");
        assertEquals(responseCreate.getText(), postCreate.getText());
        assertEquals(responseCreate.getLikes(), postCreate.getLikes());
        assertEquals(responseCreate.getTags(), postCreate.getTags());
        assertEquals(responseCreate.getUser().getId(), postCreate.getOwnerId());
    }

    @DataProvider(name = "invalidInputsForLinks")
    public Object[][] createInvalidInputsForLinks() {
        return new Object[][]{
                {randomAlphanumeric(5)},
                {randomAlphanumeric(201)}
        };
    }

    @Test(dataProvider = "invalidInputsForLinks")
    @Bug(id="", description="Api accept invalid data in fields when create a new post")
    public void createPostWithInvalidDataForLinks(String link) {

        PostPOST postCreate = PostPOST.generateRandomPost(user);
        postCreate.setLink(link);

        restWrapper.usingPosts().createItem(postCreate);
        ErrorModel responseCreate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post isn't created as link is smaller than 6 characters!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @DataProvider(name = "validInputsForLinks")
    public Object[][] createValidInputsForLinks() {
        return new Object[][]{
                {randomAlphanumeric(6)},
                {randomAlphanumeric(200)}
        };
    }

    @Test(dataProvider = "validInputsForLinks")
    @Bug(id="", description="Response status code is 200 not 201")
    public void createPostWithValidDataForLinks(String link) {
        PostPOST postCreate = PostPOST.generateRandomPost(user);
        postCreate.setLink(link);

        PostGET responseCreate = restWrapper.usingPosts().createItem(postCreate);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate post is created!");
        assertEquals(responseCreate.getText(), postCreate.getText());
        assertEquals(responseCreate.getLikes(), postCreate.getLikes());
        assertEquals(responseCreate.getTags(), postCreate.getTags());
        assertEquals(responseCreate.getUser().getId(), postCreate.getOwnerId());
        assertEquals(responseCreate.getLink(), postCreate.getLink());
    }

    @Test
    public void createPostWithoutBody() {

        restWrapper.usingPosts().createItemWithoutBody();
        ErrorModel responseCreate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post isn't created without body!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    public void createNewPostWithoutAuthorization() {

        PostPOST postCreate = PostPOST.generateRandomPost(user);

        restWrapperWithoutAuth.usingPosts().createItem(postCreate);
        ErrorModel responseCreate = restWrapperWithoutAuth.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);

        log.info("Validate post not created without app-id!");
        assertEquals(responseCreate.getError(), ErrorType.ERROR_MSG_MISSING_APP_ID);
    }
}
