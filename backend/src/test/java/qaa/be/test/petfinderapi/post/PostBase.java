package qaa.be.test.petfinderapi.post;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import qaa.be.petfinderapi.models.post.PetPost;
import qaa.be.test.petfinderapi.BasePetFinderApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public abstract class PostBase extends BasePetFinderApi {
    Map<String, Object> postBody;

    protected PetPost newRandomPost;

    protected String postId;

    @BeforeMethod()
    public void createPostInDb(ITestResult result) {
        Arrays.stream(result.getMethod().getGroups())
                .filter(group -> group.equals("create_post"))
                .findFirst()
                .ifPresent(group -> {
                    try {
                        postBody = PetPost.randomPostBodyMap(userId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    newRandomPost = createPost(postBody);
                    postId = newRandomPost.getPostId();
                });
    }

    public PetPost createPost(Map<String, Object> postBody) {
        return restWrapper.usingPosts().createPostWithMultipart(postBody);
    }

    @AfterMethod
    public void tearDown() {
        log.info("****Start Tear Down****");
        PetPost[] postList = restWrapper.usingPosts().getItems(userId);
        Arrays.stream(postList).forEach(post -> restWrapper.usingPosts().deletePost(post.getPostId()));
        log.info("****Finish Tear Down****");
    }
}
