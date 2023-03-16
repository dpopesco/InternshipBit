package qaa.be.test.dummyapi.tag;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.models.tag.TagModel;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.dummyapi.util.ErrorType;
import qaa.be.test.dummyapi.BaseDummyApi;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

public class GetTagsTest extends BaseDummyApi {
    @Test
    void testGetTags() {
        PostPOST generateRandomPost =  PostPOST.generateRandomPost(
                restWrapper.usingUsers().createItem(UserModel.generateRandomUser()));
        PostGET newPost = restWrapper.usingPosts().createItem(generateRandomPost);

        TagModel response = restWrapper.usingTags().getItems();

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertTrue(response.getData().containsAll(newPost.getTags()));
        softAssert.assertAll();
    }

    @Test
    void testGetTagsWithoutAppId() {
        restWrapperWithoutAuth.usingTags().getItems();
        restWrapperWithoutAuth.processLastError(ErrorModel.class).
                hasError(ErrorType.ERROR_MSG_MISSING_APP_ID);

        softAssert.assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);
        softAssert.assertAll();
    }

    @Test
    void testGetATagThatNotExist() {
        String myTag = RandomStringUtils.randomAlphanumeric(30);

        TagModel response = restWrapper.usingTags().getItems();
        boolean isMyTagIn = response.getData().contains(myTag);

        softAssert.assertEquals(restWrapper.getStatusCode(), SC_OK);
        softAssert.assertFalse(isMyTagIn);
        softAssert.assertAll();
    }
}
