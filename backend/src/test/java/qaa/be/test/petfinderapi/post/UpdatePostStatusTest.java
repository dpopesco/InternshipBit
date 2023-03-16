package qaa.be.test.petfinderapi.post;

import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.post.PetPost;

import java.util.HashMap;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class UpdatePostStatusTest extends PostBase {
    @Test(groups = "create_post")
    public void updatePostStatusId() {
        HashMap<String, Object> updateStatusBody = new HashMap<>();
        updateStatusBody.put("userId", userId);
        updateStatusBody.put("postStateId", "3");

        PetPost post = restWrapper.usingPosts().updatePostStatus(postId, updateStatusBody);

        assertEquals(restWrapper.getStatusCode(), SC_OK);
        assertEquals(post.getPostStateId(), "3");
    }
}
