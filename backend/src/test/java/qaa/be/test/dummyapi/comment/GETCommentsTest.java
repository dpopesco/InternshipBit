package qaa.be.test.dummyapi.comment;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.comment.CommentPOST;
import qaa.be.dummyapi.models.comment.CommentsCollection;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.dummyapi.util.ErrorType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
public class GETCommentsTest extends CommentBaseClassDummy {
    @Test
    public void getCommentsList() {

        CommentsCollection response = restWrapper.usingComments().getItems();

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate default response limit!");
        verifyResponseDataSize(response);
    }

    @Test
    public void getCommentsByPostId() {

        CommentsCollection response = restWrapper.usingComments().getCommentsByPostId(post.getId());

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate response post id as per request!");
        assertTrue(response.getData().stream().allMatch(x -> x.getPostId().equals(post.getId())));
    }

    @Test
    public void getCommentsWithInvalidPostId() {

        restWrapper.usingComments().getCommentsByPostId(randomAlphanumeric(4));
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate comments aren't shown for invalid post Id!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }

    @Test
    public void getCommentsOfDeletedPost() {

        PostPOST newPost = PostPOST.generateRandomPost(user);
        PostGET createPost = restWrapper.usingPosts().createItem(newPost);

        CommentPOST createComment = CommentPOST.generateComment(user.getId(), createPost.getId());
        restWrapper.usingPosts().deleteItem(createPost.getId());
        restWrapper.usingComments().getCommentsByPostId(createComment.getPostId());
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_NOT_FOUND);

        log.info("Validate comments aren't shown for invalid post Id!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_RSC_NOT_FOUND);
    }

    @Test
    public void getCommentsByUserId() {

        CommentsCollection response = restWrapper.usingComments().getCommentsByUserId(user.getId());

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate response user id as per request!");
        response.getData().stream().allMatch(x -> x.getOwner().getId().equals(user.getId()));
    }

    @Test
    public void getCommentsByInvalidUserId() {

        restWrapper.usingComments().getCommentsByUserId(randomAlphanumeric(4));
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate comments aren't shown for invalid user Id!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }

    @Test
    public void getCommentsOfDeletedUser() {

        UserModel createUser = restWrapper.usingUsers().createItem(UserModel.generateRandomUser());
        post.setUser(createUser);

        CommentPOST createComment = CommentPOST.generateComment(createUser.getId(), post.getId());
        restWrapper.usingUsers().deleteItem(createUser.getId());
        restWrapper.usingComments().getCommentsByUserId(createComment.getOwnerId());
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_NOT_FOUND);

        log.info("Validate comments aren't shown for invalid post Id!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_RSC_NOT_FOUND);
    }
}
