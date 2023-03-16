package qaa.be.test.petfinderapi.pettypes;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.pettypes.PetTypes;
import qaa.be.test.petfinderapi.BasePetFinderApi;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GetPetTypesTest extends BasePetFinderApi {
    @Test
    public void getUserById() {
        PetTypes responseGet = restWrapper.usingPetTypes().getItems();
        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);
    }
}
