package qaa.be.petfinderapi.requests;

import io.restassured.response.Response;
import org.springframework.http.HttpMethod;
import qaa.be.core.RestRequest;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.core.requests.ModelRequest;
import qaa.be.petfinderapi.PetFinderWrapper;
import qaa.be.petfinderapi.models.post.FilteredPosts;
import qaa.be.petfinderapi.models.post.PetPost;

import java.util.Map;

public class PostsRequests extends ModelRequest<PostsRequests, PetFinderWrapper>
        implements APIContractPet<PetPost> {

    private static final String POST_PATH = "/posts/v1";
    private static final String PARAMS = "/{params}";

    public PostsRequests(PetFinderWrapper restWrapper) {
        super(restWrapper);
    }

    @Override
    public PetPost getItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, POST_PATH + PARAMS, itemId);
        return restWrapper.processModel(PetPost.class, request);
    }

    @Override
    public PetPost createItem(PetPost item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, POST_PATH);
        return restWrapper.processModel(PetPost.class, request);
    }

    @Override
    public PetPost createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, POST_PATH);

        return restWrapper.sendRequest(request).as(PetPost.class);
    }

    public PetPost[] getItems(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, POST_PATH + "/user/{parameters}",
                itemId);
        return restWrapper.sendRequest(request).as(PetPost[].class);
    }

    public PetPost updateItem(String itemId, PetPost updatedItem) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatedItem, POST_PATH + PARAMS,
                itemId);
        return restWrapper.processModel(PetPost.class, request);
    }

    public Response updatePostNoToken(String itemId, PetPost updatedItem) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatedItem, POST_PATH + PARAMS,
                itemId);
        return restWrapper.sendRequest(request);
    }

    public PetPost updatePostStatus(String postId, Object updatePostStatus) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatePostStatus, POST_PATH + "/{params}/status",
                postId);
        return restWrapper.processModel(PetPost.class, request);
    }

    public Response deletePost(String postId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, POST_PATH + PARAMS,
                postId);
        return restWrapper.sendRequest(request);
    }

    public FilteredPosts getAllPostBy(String postStateId, String petTypeId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, POST_PATH +
                        "/postState/{postStateId}/petTypes/{petTypeId}?{parameters}",
                postStateId, petTypeId, this.getParameters());
        return restWrapper.processModel(FilteredPosts.class, request);
    }

    public PetPost createPostWithMultipart(Map<String, Object> body) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, POST_PATH);
        request.setPostMultipartBody(body);

        return restWrapper.processModel(PetPost.class, request);
    }

    public Response createPostWithMultipartError(Map<String, Object> body) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, POST_PATH);
        request.setPostMultipartBody(body);

        return restWrapper.sendRequest(request);
    }
}
