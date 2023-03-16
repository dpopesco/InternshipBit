package qaa.be.test.petfinderapi.post;

import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.post.PetPost;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class GetPostByIdTest extends PostBase {
    @Test(groups = "create_post")
    public void testGetPostById() {
        PetPost post = restWrapper.usingPosts().getItem(postId);

        assertEquals(restWrapper.getStatusCode(), SC_OK);
        assertEquals(newRandomPost.getPostId(), post.getPostId());
    }
}
