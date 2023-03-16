package qaa.be.test.petfinderapi.post;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.post.PetPost;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.testng.Assert.assertEquals;

public class EditPostTest extends PostBase {
    @Test(groups = "create_post")
    public void testEditPost() {
        PetPost postUpdateBody = PetPost.randomUpdatePostBody(postId);
        PetPost updatedPost = restWrapper.usingPosts().updateItem(postId, postUpdateBody);

        assertEquals(restWrapper.getStatusCode(), SC_OK);
        assertEquals(updatedPost.getPostId(), postId);
        assertEquals(updatedPost.getPetName(), postUpdateBody.getPetName());
        assertEquals(updatedPost.getCoordinateLatitude(), postUpdateBody.getCoordinateLatitude());
        assertEquals(updatedPost.getCoordinateLongitude(), postUpdateBody.getCoordinateLongitude());
        assertEquals(updatedPost.getCoordinateLongitude(), postUpdateBody.getCoordinateLongitude());
        assertEquals(updatedPost.getArea(), postUpdateBody.getArea());
        assertEquals(updatedPost.getPetTypeId(), postUpdateBody.getPetTypeId());
        assertEquals(updatedPost.getDescription(), postUpdateBody.getDescription());

    }

    @Test(groups = "create_post")
    public void testEditPostWithoutToken() {
        PetPost postUpdateBody = PetPost.randomUpdatePostBody(postId);
        restWrapperWithoutToken.usingPosts().updatePostNoToken(postId, postUpdateBody);

        assertEquals(restWrapperWithoutToken.getStatusCode(), SC_UNAUTHORIZED);
    }
}
