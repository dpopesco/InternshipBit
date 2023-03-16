package qaa.be.test.petfinderapi.post;

import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.testng.Assert.assertEquals;

public class DeletePostTest extends PostBase {
    @Test(groups = "create_post")
    public void deletePost() {

        // empty response
        restWrapper.usingPosts().deletePost(postId);

        assertEquals(restWrapper.getStatusCode(), SC_OK);
    }

    @Test(groups = "create_post")
    public void deletePostWithoutToken() {
        restWrapperWithoutToken.usingPosts().deletePost(postId);

        assertEquals(restWrapperWithoutToken.getStatusCode(), SC_UNAUTHORIZED);
    }
}
