package qaa.be.test.dummyapi.post;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.dummyapi.models.error.ErrorModel;
import qaa.be.dummyapi.models.post.PostsCollection;
import qaa.internship.util.Bug;
import qaa.be.dummyapi.util.ErrorType;
import qaa.be.dummyapi.util.PaginationDataProvider;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GETPostsPaginationTest extends PostBaseClassDummy {
    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "valid_limit_and_page_values")
    public void checkPageNumberAndLimitParameters(int limit, int page) {

        PostsCollection post = restWrapper.usingPosts().usingParams("limit=" + limit, "page=" + page).getItems();

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate provided limit and page with response list limit and page");
        verifyResponseDataSize(post);
        assertEquals(post.getPage(), page);
    }

    @Test
    public void checkPostsDefaultLimit() {

        PostsCollection post = restWrapper.usingPosts().getItems();

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate default response limit!");
        verifyResponseDataSize(post);
    }

    @Bug(id = "", description = "bug, api accepts invalid page parameter")
    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "invalid_page_values")
    public void checkInvalidPageNumber(Object pageNumber) {

        restWrapper.usingPosts().usingParams("page=" + String.valueOf(pageNumber)).getItems();
        ErrorModel post = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate params not valid");
        assertEquals(post.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);
    }

    @Bug(id = "", description = "bug, api accepts invalid limit parameter")
    @Test(dataProviderClass = PaginationDataProvider.class, dataProvider = "invalid_limit_values")
    public void checkInvalidLimitNumber(Object limitNumber) {

        restWrapper.usingPosts().usingParams("limit=" + String.valueOf(limitNumber)).getItems();
        ErrorModel post = restWrapper.processLastError(ErrorModel.class);

        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_BAD_REQUEST);

        log.info("Validate params not valid");
        assertEquals(post.getError(), ErrorType.ERROR_MSG_PARAMS_NOT_VALID);

        log.error("BUG, api accepts invalid page parameter");
    }
}
