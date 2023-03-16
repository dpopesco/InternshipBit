package qaa.be.test.dummyapi.post;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.models.post.PostsCollection;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
public class GETPostsTest extends PostBaseClassDummy {

    @Test
    public void getUserPostsById() {

        PostPOST generateRandomPost = PostPOST.generateRandomPost();
        generateRandomPost.setOwnerId(user.getId());
        PostGET createPost = restWrapper.usingPosts().createItem(generateRandomPost);

        PostGET responsePost = restWrapper.usingPosts().getItem(createPost.getId());

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post response as per request!");
        assertEquals(responsePost.getId(), createPost.getId());
        assertEquals(responsePost.getLikes(), createPost.getLikes());
        assertEquals(responsePost.getTags(), createPost.getTags());
        assertEquals(responsePost.getUser().getId(), createPost.getUser().getId());
    }

    @Test
    public void getUserPostsByInvalidPostId() {

        restWrapper.usingPosts().getItem(randomAlphanumeric(4));
        ErrorModel responsePost = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate response post id as per request!");
        assertEquals(responsePost.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }

    @Test
    public void getUserPostsByUserId() {

        PostPOST generateRandomPost = PostPOST.generateRandomPost();
        generateRandomPost.setOwnerId(user.getId());

        //create posts for one user, have same information, but ids are different
        restWrapper.usingPosts().createItem(generateRandomPost);
        restWrapper.usingPosts().createItem(generateRandomPost);

        PostsCollection responsePost = restWrapper.usingPosts().getInfoByUserId(user.getId());

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate response owner id as per request!");
        assertTrue(responsePost.getData().stream().allMatch(x -> x.getUser().getId().equals(user.getId())));
    }

    @Test
    public void getUserPostsByInvalidUserId() {

        restWrapper.usingPosts().getInfoByUserId(randomAlphanumeric(3));
        ErrorModel responsePost = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate response owner id as per request!");
        responsePost.hasError(responsePost.getError());
    }

    @Test
    public void getPostsByTag() {

        String tag = "ice";
        PostsCollection responsePost = restWrapper.usingPosts().getInfoByTag(tag);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate response tag as per request!");
        assertTrue(responsePost.getData().stream().allMatch(x -> x.getTags().contains(tag)));
    }

    @Bug(id = "", description = "bug, it shows an empty list")
    @Test
    public void getInfoByInvalidTag() {

        restWrapper.usingPosts().getInfoByTag(randomAlphabetic(4));
        ErrorModel responsePost = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate response tag as per request!");
        assertEquals(responsePost.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }

    @Test(priority = 1)
    public void checkItemsCreatedInCurrentEnvironment() {

        restWrapper.usingPosts().usingParams(CREATED_ITEMS_PARAMS).getItems();

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);
    }
}
