package qaa.be.dummyapi.requests;

import org.springframework.http.HttpMethod;
import qaa.be.core.RestRequest;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.core.requests.ModelRequest;
import qaa.be.dummyapi.DummyApiWrapper;
import qaa.be.dummyapi.models.post.Post;
import qaa.be.dummyapi.models.post.PostGET;
import qaa.be.dummyapi.models.post.PostPOST;
import qaa.be.dummyapi.models.post.PostsCollection;

public class PostsRequests extends ModelRequest<PostsRequests, DummyApiWrapper> implements APIContract<Post, PostsCollection> {

    private final String path = "/post/{id}";
    private final String pathCreate = "/post/create";

    public PostsRequests(DummyApiWrapper restWrapper) {
        super(restWrapper);
    }

    @Override
    public PostGET getItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, itemId);
        return restWrapper.processModel(PostGET.class, request);
    }

    @Override
    public PostsCollection getItems() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/post?{parameters}", this.getParameters());
        return restWrapper.processModel(PostsCollection.class, request);
    }

    @Override
    public PostGET createItem(Post item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, pathCreate);
        return restWrapper.processModel(PostGET.class, request);
    }

    @Override
    public Post createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, pathCreate);
        return restWrapper.processModel(Post.class, request);
    }

    @Override
    public PostGET updateItem(String itemId, Post updatedItem) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatedItem, path, itemId);
        return restWrapper.processModel(PostGET.class, request);
    }

    @Override
    public PostPOST deleteItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, itemId);
        return restWrapper.processModel(PostPOST.class, request);
    }

    public PostsCollection getInfoByUserId(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user/{id}/post", userId);
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostsCollection getInfoByTag(String tag) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/tag/{tag}/post", tag);
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostGET createItem(String json) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, json, pathCreate);
        return restWrapper.processModel(PostGET.class, request);
    }

    public PostGET updateItem(String json, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, json, path, postId);
        return restWrapper.processModel(PostGET.class, request);
    }
}
