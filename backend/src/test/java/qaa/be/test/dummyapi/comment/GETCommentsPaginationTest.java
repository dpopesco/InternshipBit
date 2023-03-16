package qaa.be.test.dummyapi.comment;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.comment.CommentsCollection;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;
import qaa.be.dummyapi.util.PaginationDataProvider;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GETCommentsPaginationTest extends CommentBaseClassDummy {
    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "valid_limit_and_page_values")
    public void checkPageNumberAndLimitParameters(int limit, int page) {

        CommentsCollection comment = restWrapper.usingComments().usingParams("limit=" + limit, "page=" + page).getItems();

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate provided limit with response list limit");
        verifyResponseDataSize(comment);
        assertEquals(comment.getPage(), page);
    }

    @Test
    public void checkCommentsDefaultLimit() {

        CommentsCollection response = restWrapper.usingComments().getItems();

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate default response limit!");
        verifyResponseDataSize(response);
    }

    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "invalid_page_values")
    @Bug(id = "", description = "bug, api accepts invalid page parameter")
    public void checkInvalidPageNumber(Object pageNumber) {

        restWrapper.usingComments().usingParams("page=" + String.valueOf(pageNumber)).getItems();
        ErrorModel errorResponse = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate params not valid");
        assertEquals(errorResponse.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }


    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "invalid_limit_values")
    @Bug(id = "", description = "bug, api accepts invalid limit parameter")
    public void checkInvalidLimitNumber(Object limitNumber) {

        restWrapper.usingComments().usingParams("limit=" + String.valueOf(limitNumber)).getItems();
        ErrorModel errorResponse = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate params not valid!");
        assertEquals(errorResponse.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
        log.error("BUG, api accepts invalid page parameter");
    }
}
