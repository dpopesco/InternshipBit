package qaa.be.test.dummyapi.post;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.models.error.PostErrorModel;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

@Slf4j
public class PUTPostsTest extends PostBaseClassDummy {

    @Test
    public void updatePost() {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        PostPOST updatePost = PostPOST.generateRandomPost();
        PostGET responseUpdate = restWrapper.usingPosts().updateItem(responseCreate.getId(), updatePost);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post is updated successfully!");
        assertEquals(responseUpdate.getText(), updatePost.getText());
        assertEquals(responseUpdate.getImage(), updatePost.getImage());
        assertEquals(responseUpdate.getLikes(), updatePost.getLikes());
        assertEquals(responseUpdate.getTags(), updatePost.getTags());
    }

    @Test
    public void updateAnInvalidPostId() {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        restWrapper.usingPosts().createItem(createPost);

        restWrapper.usingPosts().updateItem(randomAlphanumeric(6), createPost);
        ErrorModel responseUpdate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate postId is not valid!");
        assertEquals(responseUpdate.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }

    @Test
    public void updateADeletedPostId() {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);
        restWrapper.usingPosts().deleteItem(responseCreate.getId());

        restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);
        PostErrorModel responseUpdate = restWrapper.processLastError(PostErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate postId is deleted, and cannot be updated");
        assertEquals(responseUpdate.getError(), ErrorType.ERROR_MSG_BODY);
        assertNull(responseUpdate.getData().getId());
    }

    @Test
    public void updateUserFromPost() {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        createPost.setOwnerId(randomAlphanumeric(5));
        PostGET responseUpdate = restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post is not updated with owner id!");
        assertNotEquals(responseUpdate.getUser().getId(), createPost.getOwnerId());
        assertEquals(responseUpdate.getUser().getId(), user.getId());
    }

    @Test
    public void updatePostWithAlreadyDeletedUser() {

        UserModel createUser = restWrapper.usingUsers().createItem(UserModel.generateRandomUser());
        PostPOST createPost = PostPOST.generateRandomPost(createUser);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        UserModel deleteUser = restWrapper.usingUsers().deleteItem(createUser.getId());
        createPost.setOwnerId(deleteUser.getId());

        restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);
        PostErrorModel responseUpdate = restWrapper.processLastError(PostErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post is not updated with deleted owner id!");
        assertEquals(responseUpdate.getError(), ErrorType.ERROR_MSG_BODY);
        assertNull(responseUpdate.getData().getId());
    }

    @Test
    public void updatePostWithoutAuthorization() {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        restWrapperWithoutAuth.usingPosts().updateItem(responseCreate.getId(), createPost);
        ErrorModel responseUpdate = restWrapperWithoutAuth.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);

        log.info("Validate post cannot be updated without app id!");
        assertEquals(responseUpdate.getError(), ErrorType.ERROR_MSG_MISSING_APP_ID);
    }

    @Bug(id = "", description = "bug, api accepts text having (1000 < text field < 6) ")
    @Test(dataProviderClass = POSTPostsTest.class, dataProvider = "invalidInputsForText")
    public void updatePostWithInvalidDataForText(String text) {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        createPost.setText(text);
        restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);
        ErrorModel responseUpdate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post is not updated as text has more than 1000 characters!");
        log.error("Bug, api accepts text bigger than 1000!");
        assertEquals(responseUpdate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test(dataProviderClass = POSTPostsTest.class, dataProvider = "validInputsForText")
    public void updatePostWithValidDataForText(String text) {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        createPost.setText(text);
        PostGET responseUpdate = restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post is updated!");
        assertEquals(responseUpdate.getText(), createPost.getText());
        assertEquals(responseUpdate.getLikes(), createPost.getLikes());
        assertEquals(responseUpdate.getTags(), createPost.getTags());
        assertEquals(responseUpdate.getUser().getId(), createPost.getOwnerId());
    }

    @Test
    public void updatePostWithXSSForText() {

        PostPOST postCreate = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(postCreate);

        postCreate.setText(XSS_INJECTION);
        PostGET responseUpdate = restWrapper.usingPosts().updateItem(responseCreate.getId(), postCreate);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post is updated when entering xss script for text field!");
        assertEquals(responseUpdate.getText(), postCreate.getText());
    }

    @Bug(id = "", description = "post can be updated with double, negative and maxvalue + 1")
    @Test(dataProviderClass = POSTPostsTest.class, dataProvider = "invalidInputsForLikes")
    public void updatePostWithInvalidInputsForLikes(Object likes) {

        PostPOST createPost = new PostPOST();
        createPost.setOwnerId(user.getId());
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        JSONObject createPostToJson = new JSONObject(createPost);
        createPostToJson.put("likes", likes);
        restWrapper.usingPosts().updateItem(createPostToJson.toString(), responseCreate.getId());
        PostErrorModel responseUpdate = restWrapper.processLastError(PostErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post cannot be updated when sent characters for number of likes!");
        assertEquals(responseUpdate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test(dataProviderClass = POSTPostsTest.class, dataProvider = "validInputsForLikes")
    public void updatePostWithValidInputsForLikes(int likes) {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        createPost.setLikes(likes);
        PostGET responseUpdate = restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post is updated when sent valid numbers for likes!");
        assertEquals(responseUpdate.getText(), createPost.getText());
        assertEquals(responseUpdate.getLikes(), createPost.getLikes());
        assertEquals(responseUpdate.getTags(), createPost.getTags());
        assertEquals(responseUpdate.getUser().getId(), createPost.getOwnerId());
    }

    @Bug(id = "", description = "post is updated with invalid data for links")
    @Test(dataProviderClass = POSTPostsTest.class, dataProvider = "invalidInputsForLinks")
    public void updatePostWithInvalidInputsForLinks(String link) {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        createPost.setLink(link);

        restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);
        ErrorModel responseUpdate = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post is updated when sent valid numbers for likes!");
        assertEquals(responseUpdate.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test(dataProviderClass = POSTPostsTest.class, dataProvider = "validInputsForLinks")
    public void updatePostWithValidInputsForLinks(String link) {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        createPost.setLink(link);
        PostGET responseUpdate = restWrapper.usingPosts().updateItem(responseCreate.getId(), createPost);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post is updated when sent valid numbers for likes!");
        assertEquals(responseUpdate.getText(), createPost.getText());
        assertEquals(responseUpdate.getLikes(), createPost.getLikes());
        assertEquals(responseUpdate.getTags(), createPost.getTags());
        assertEquals(responseUpdate.getUser().getId(), createPost.getOwnerId());
        assertEquals(responseUpdate.getLink(), createPost.getLink());
    }
}
