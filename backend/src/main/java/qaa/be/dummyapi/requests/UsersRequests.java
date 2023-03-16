package qaa.be.dummyapi.requests;

import org.springframework.http.HttpMethod;
import qaa.be.core.RestRequest;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.core.requests.ModelRequest;
import qaa.be.dummyapi.DummyApiWrapper;
import qaa.be.dummyapi.models.user.UserModel;
import qaa.be.dummyapi.models.user.UsersCollection;

public class UsersRequests extends ModelRequest<UsersRequests, DummyApiWrapper> implements APIContract<UserModel, UsersCollection> {

    private final String PATH_USER_WITH_PARAMS = "user?{params}";
    private final String PATH_USER_WITH_ENDPOINT = "user/{params}";

    public UsersRequests(DummyApiWrapper restWrapper) {
        super(restWrapper);
    }

    @Override
    public UserModel getItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, PATH_USER_WITH_ENDPOINT, itemId);
        return restWrapper.sendRequestAndCreateModel(UserModel.class, request);
    }

    @Override
    public UsersCollection getItems() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, PATH_USER_WITH_PARAMS,
                getParameters());
        return restWrapper.sendRequestAndCreateModel(UsersCollection.class, request);
    }

    @Override
    public UserModel createItem(UserModel item) throws ConversionJsonToModelException {
        RestRequest postRequest = RestRequest.requestWithBody(HttpMethod.POST,
                item,
                PATH_USER_WITH_ENDPOINT, "create");
        return restWrapper.sendRequestAndCreateModel(UserModel.class, postRequest);
    }

    @Override
    public UserModel createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest postRequest = RestRequest.simpleRequest(HttpMethod.POST,
                "user/create");
        return restWrapper.sendRequestAndCreateModel(UserModel.class, postRequest);
    }

    @Override
    public UserModel updateItem(String itemId, UserModel updatedItem) throws ConversionJsonToModelException {
        RestRequest updateRequest = RestRequest.requestWithBody(HttpMethod.PUT,
                updatedItem, PATH_USER_WITH_ENDPOINT, itemId);
        return restWrapper.sendRequestAndCreateModel(UserModel.class, updateRequest);
    }

    @Override
    public UserModel deleteItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, PATH_USER_WITH_ENDPOINT,
                itemId);
        return restWrapper.sendRequestAndCreateModel(UserModel.class, request);
    }
}