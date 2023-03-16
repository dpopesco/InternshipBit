package qaa.be.dummyapi.requests;

import org.springframework.http.HttpMethod;
import qaa.be.core.RestRequest;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.core.requests.ModelRequest;
import qaa.be.dummyapi.DummyApiWrapper;
import qaa.be.dummyapi.models.tag.TagModel;

public class TagsRequests extends ModelRequest<TagsRequests, DummyApiWrapper> {

    private static final String TAG_ENDPOINT = "tag";

    public TagsRequests(DummyApiWrapper restWrapper) {
        super(restWrapper);
    }

    public TagModel getItems() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, TAG_ENDPOINT);
        return restWrapper.processModel(TagModel.class, request);
    }

}
