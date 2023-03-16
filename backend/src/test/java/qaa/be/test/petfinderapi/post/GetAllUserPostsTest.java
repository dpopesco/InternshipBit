package qaa.be.test.petfinderapi.post;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.post.PetPost;

import java.io.IOException;
import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GetAllUserPostsTest extends PostBase {
    int numbersOfPosts;

    Random random = new Random();

    @BeforeClass(alwaysRun = true)
    public void createPostsOnCurrentUser() throws IOException {
        numbersOfPosts = random.nextInt(4);
        for (int i = 1; i <= numbersOfPosts; i++) {
            postBody = PetPost.randomPostBodyMap(userId);
            createPost(postBody);
        }
    }

    @Test
    public void checkAllUserPosts() {
        PetPost[] postList = restWrapper.usingPosts().getItems(userId);

        assertEquals(restWrapper.getStatusCode(), SC_OK);
        assertEquals(postList.length, numbersOfPosts);
    }
}
