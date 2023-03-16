package qaa.be.test.dummyapi.comment;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.comment.Comment;
import qaa.be.dummyapi.models.comment.CommentPOST;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.util.ErrorType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Slf4j
public class DELETECommentsTest extends CommentBaseClassDummy {

    @Test
    public void deleteComment() {

        Comment responseCreate = restWrapper.usingComments().createItem(CommentPOST.generateComment(user.getId(), post.getId()));
        Comment commentResponse = restWrapper.usingComments().deleteItem(responseCreate.getId());

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate comment is deleted successfully!");
        assertNull(commentResponse.getMessage());
    }

    @Test
    public void deleteAlreadyDeletedComment() {

        Comment responseCreate = restWrapper.usingComments().createItem(CommentPOST.generateComment(user.getId(), post.getId()));
        Comment responseDelete = restWrapper.usingComments().deleteItem(responseCreate.getId());

        restWrapper.usingComments().deleteItem(responseDelete.getId());
        ErrorModel errorResponse = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_NOT_FOUND);

        log.info("Validate comment is already deleted!");
        assertEquals(errorResponse.getError(), ErrorType.ERROR_MSG_RSC_NOT_FOUND);
    }

    @Test
    public void deleteCommentWithoutAuthorization() {

        restWrapperWithoutAuth.usingComments().deleteItem(randomAlphanumeric(3));
        ErrorModel response = restWrapperWithoutAuth.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapperWithoutAuth.getStatusCode(), SC_FORBIDDEN);

        log.info("Validate comment cannot be deleted without authorization!");
        assertEquals(response.getError(), ErrorType.ERROR_MSG_MISSING_APP_ID);
    }
}
