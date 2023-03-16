package qaa.be.test.dummyapi.comment;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.comment.CommentGET;
import qaa.be.dummyapi.models.comment.CommentPOST;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;

@Slf4j
public class POSTCommentsTest extends CommentBaseClassDummy {
    @Test
    @Bug(id="", description="Response status code received is 200 not 201")
    public void createComment() {

        CommentPOST createComment = CommentPOST.generateComment(user.getId(), post.getId());
        CommentGET responseComment = restWrapper.usingComments().createItem(createComment);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate comment is created!");
        assertEquals(responseComment.getMessage(), createComment.getMessage());
        assertEquals(responseComment.getOwner().getId(), createComment.getOwnerId());
        assertEquals(responseComment.getPostId(), createComment.getPostId());
    }

    @DataProvider(name = "validInputsForMessage")
    public Object[][] createValidInputsForMsg() {
        return new Object[][]{
                {randomAlphanumeric(2)},
                {randomAlphanumeric(500)}
        };
    }

    @Test(dataProvider = "validInputsForMessage")
    @Bug(id="", description="Response status code received is 200 not 201")
    public void createCommentWithValidDataForMessage(String msg) {

        CommentPOST createComment = CommentPOST.generateComment(user.getId(), post.getId());
        createComment.setMessage(msg);
        CommentGET responseComment = restWrapper.usingComments().createItem(createComment);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate comment is created!");
        assertEquals(responseComment.getMessage(), createComment.getMessage());
        assertEquals(responseComment.getOwner().getId(), createComment.getOwnerId());
        assertEquals(responseComment.getPostId(), createComment.getPostId());
    }

    @DataProvider(name = "invalidInputsForMessage")
    public Object[][] createInvalidInputsForMsg() {
        return new Object[][]{
                {randomAlphanumeric(1)},
                {randomAlphanumeric(501)}
        };
    }

    @Bug(id = "", description = "bug, api accepts text having (500 < text field < 2) ")
    @Test(dataProvider = "invalidInputsForMessage")
    public void createCommentWithInvalidDataForMessage(String msg) {

        CommentPOST createComment = CommentPOST.generateComment(user.getId(), post.getId());
        createComment.setMessage(msg);

        restWrapper.usingComments().createItem(createComment);
        ErrorModel responseComment = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate comment isn't created!");
        assertEquals(responseComment.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    public void createCommentWithInvalidOwnerId() {

        CommentPOST createComment = CommentPOST.generateComment(randomAlphanumeric(4), post.getId());

        restWrapper.usingComments().createItem(createComment);
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate comment isn't created!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    public void createCommentWithInvalidPostId() {

        CommentPOST createComment = CommentPOST.generateComment(user.getId(), randomAlphanumeric(4));

        restWrapper.usingComments().createItem(createComment);
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate comment isn't created!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    @Bug(id="", description="Response status code received is 200 not 201")
    public void createCommentWithEmptyMessage() {

        CommentPOST body = new CommentPOST();
        body.setOwnerId(user.getId());
        body.setPostId(post.getId());
        CommentGET response = restWrapper.usingComments().createItem(body);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_CREATED);

        log.info("Validate comment is created!");
        assertEquals(response.getMessage(), "");
    }

    @Test
    public void createCommentWithInvalidOwnerIdAndPostId() {

        CommentPOST body = CommentPOST.generateComment(randomAlphanumeric(4), randomAlphanumeric(4));

        restWrapper.usingComments().createItem(body);
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate comment isn't created!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_BODY);
    }

    @Test
    public void createCommentWithoutAuthorization() {

        CommentPOST body = CommentPOST.generateComment(user.getId(), post.getId());

        restWrapperWithoutAuth.usingComments().createItem(body);
        ErrorModel response = restWrapperWithoutAuth.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);

        log.info("Validate comment isn't created without authorization!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_MISSING_APP_ID);
    }

    @Test
    public void createCommentWithoutBody() {

        restWrapper.usingComments().createItemWithoutBody();
        ErrorModel response = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate comment isn't created without body!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_BODY);
    }
}
