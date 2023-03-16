package qaa.be.test.dummyapi.comment;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.test.dummyapi.BaseDummyApi;

@Slf4j
public class CommentBaseClassDummy extends BaseDummyApi {

    protected UserModel user;
    protected PostGET post;

    @BeforeClass(alwaysRun = true)
    protected void createPost() {
        log.info("New user is created.");
        user = restWrapper.usingUsers().createItem(UserModel.generateRandomUser());

        log.info("New post is created.");
        PostPOST createPost = PostPOST.generateRandomPost();
        createPost.setOwnerId(user.getId());
        post = restWrapper.usingPosts().createItem(createPost);
    }
}
