package qaa.be.test.dummyapi.post;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.util.ErrorType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Slf4j
public class DELETEPostsTest extends PostBaseClassDummy {
    @Test
    public void deletePost() {

        PostPOST createPost = PostPOST.generateRandomPost(user);
        PostGET responseCreate = restWrapper.usingPosts().createItem(createPost);

        PostPOST responseDelete = restWrapper.usingPosts().deleteItem(responseCreate.getId());

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post is deleted successfully!");
        assertNull(responseDelete.getLink());
    }

    @Test
    public void deleteAlreadyDeletedPost() {

        PostPOST post = PostPOST.generateRandomPost(user);

        PostGET responseCreate = restWrapper.usingPosts().createItem(post);
        PostPOST responseDelete = restWrapper.usingPosts().deleteItem(responseCreate.getId());

        restWrapper.usingPosts().deleteItem(responseDelete.getId());
        ErrorModel responseAlreadyDelete = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_NOT_FOUND);

        log.info("Validate already deleted post is not found!");
        assertEquals(responseAlreadyDelete.getError(), ErrorType.ERROR_MSG_RSC_NOT_FOUND);
    }

    @Test
    public void deleteAnInvalidPostId() {

        restWrapper.usingPosts().deleteItem(randomAlphanumeric(4));
        ErrorModel responseDelete = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate post is not deleted because of invalid postId!");
        assertEquals(responseDelete.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }

    @Test
    public void deletePostWithoutAuthorization() {

        PostPOST post = PostPOST.generateRandomPost(user);

        PostGET responseCreate = restWrapper.usingPosts().createItem(post);

        restWrapperWithoutAuth.usingPosts().deleteItem(responseCreate.getId());
        ErrorModel responseDelete = restWrapperWithoutAuth.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);

        log.info("Validate post is not deleted as app id is missing!");
        assertEquals(responseDelete.getError(), ErrorType.ERROR_MSG_MISSING_APP_ID);
    }
}
