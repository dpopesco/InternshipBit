package qaa.be.test.petfinderapi.post;

import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.post.PetPost;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.testng.Assert.assertEquals;

public class CreatePostTest extends PostBase {
    @Test(groups = "create_post")
    public void testCreatePost() throws IOException {
        postBody = PetPost.randomPostBodyMap(userId);
        PetPost newPost = restWrapper.usingPosts().createPostWithMultipart(postBody);

        assertEquals(restWrapper.getStatusCode(), SC_OK);
        assertEquals(newPost.getPetName(), postBody.get("petName"));
        assertEquals(newPost.getPetColor(), postBody.get("petColor"));
        assertEquals(newPost.getDescription(), postBody.get("description"));
        assertEquals(newPost.getCoordinateLongitude(), postBody.get("coordinateLongitude"));
        assertEquals(newPost.getCoordinateLatitude(), postBody.get("coordinateLatitude"));
    }

    @Test
    public void testCreatePostWithoutToken() throws IOException {
        postBody = PetPost.randomPostBodyMap(userId);
        restWrapperWithoutToken.usingPosts().createPostWithMultipartError(postBody);

        assertEquals(restWrapperWithoutToken.getStatusCode(), SC_UNAUTHORIZED);
    }
}
