package qaa.be.dummyapi.requests;

import org.springframework.http.HttpMethod;
import qaa.be.core.RestRequest;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.core.requests.ModelRequest;
import qaa.be.dummyapi.DummyApiWrapper;
import qaa.be.dummyapi.models.comment.Comment;
import qaa.be.dummyapi.models.comment.CommentGET;
import qaa.be.dummyapi.models.comment.CommentsCollection;

public class CommentsRequests extends ModelRequest<CommentsRequests, DummyApiWrapper> implements APIContract<Comment, CommentsCollection> {

    private final String pathPost = "/post/{id}/comment";
    private final String pathUser = "/user/{id}/comment";
    private final String pathCreate = "/comment/create";
    private final String pathDelete = "/comment/{id}";
    private final String pathWithParameters = "/comment?{parameters}";

    public CommentsRequests(DummyApiWrapper restWrapper) {
        super(restWrapper);
    }

    @Override
    public CommentsCollection getItems() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, pathWithParameters, this.getParameters());
        return restWrapper.processModel(CommentsCollection.class, request);
    }

    @Override
    public CommentGET createItem(Comment item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, pathCreate);
        return restWrapper.processModel(CommentGET.class, request);
    }

    @Override
    public Comment createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, pathCreate);
        return restWrapper.processModel(Comment.class, request);
    }

    @Override
    public CommentGET deleteItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, pathDelete, itemId);
        return restWrapper.processModel(CommentGET.class, request);
    }

    //For comment endpoint api doesn't have get by commentId and update commentId requests, therefore methods are not implemented
    @Override
    public Comment getItem(String itemId) throws ConversionJsonToModelException {
        return null;
    }

    @Override
    public Comment updateItem(String itemId, Comment updatedItem) throws ConversionJsonToModelException {
        return null;
    }

    public CommentsCollection getCommentsByPostId(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, pathPost, postId);
        return restWrapper.processModel(CommentsCollection.class, request);
    }

    public CommentsCollection getCommentsByUserId(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, pathUser, userId);
        return restWrapper.processModel(CommentsCollection.class, request);
    }
}
