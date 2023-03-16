package qaa.be.petfinderapi.requests;

import org.springframework.http.HttpMethod;
import qaa.be.core.RestRequest;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.core.requests.ModelRequest;
import qaa.be.petfinderapi.PetFinderWrapper;
import qaa.be.petfinderapi.models.pettypes.PetTypes;

public class PetTypesRequests extends ModelRequest<PetTypesRequests, PetFinderWrapper> {
    public PetTypesRequests(PetFinderWrapper restWrapper) {
        super(restWrapper);
    }

    public PetTypes getItems() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/pettypes/v1");
        return restWrapper.processModel(PetTypes.class, request);
    }
}
