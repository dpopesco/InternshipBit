package qaa.be.petfinderapi.requests;

import org.springframework.http.HttpMethod;
import qaa.be.core.RestRequest;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.core.requests.ModelRequest;
import qaa.be.petfinderapi.PetFinderWrapper;
import qaa.be.petfinderapi.models.user.PetUser;

public class UsersRequests extends ModelRequest<UsersRequests, PetFinderWrapper> implements APIContractPet<PetUser> {
    private static final String USER_REGISTER_PATH = "/users/v1";
    private static final String USER_LOGIN_PATH = "/users/v1/login";
    private static final String USER_BY_ID_PATH = "/users/v1/{id}";

    public UsersRequests(PetFinderWrapper restWrapper) {
        super(restWrapper);
    }

    @Override
    public PetUser getItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, USER_BY_ID_PATH, itemId);
        return restWrapper.processModel(PetUser.class, request);
    }

    @Override
    public PetUser createItem(PetUser item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, USER_REGISTER_PATH);
        return restWrapper.processModel(PetUser.class, request);
    }

    @Override
    public PetUser createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, USER_REGISTER_PATH);
        return restWrapper.processModel(PetUser.class, request);
    }

    public PetUser createLogin(PetUser item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, USER_LOGIN_PATH);
        return restWrapper.processModel(PetUser.class, request);
    }

    public PetUser createLoginWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, USER_LOGIN_PATH);
        return restWrapper.processModel(PetUser.class, request);
    }

}
