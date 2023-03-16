package qaa.be.test.petfinderapi.user;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.user.PetUser;
import qaa.be.test.petfinderapi.BasePetFinderApi;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GetUserTest extends BasePetFinderApi {
    @Test
    public void getUserById() {
        PetUser userDetails = loginWithRandomUser();
        PetUser responseGet = restWrapper.usingUsers().getItem(userId);
        log.info("Validate status code!");
        assertEquals(restWrapper.getStatusCode(), SC_OK);

        log.info("Validate post response as per request!");
        assertEquals(responseGet.getId(), userDetails.getId());
        assertEquals(responseGet.getEmail(), userDetails.getEmail());
        assertEquals(responseGet.getFirstName(), userDetails.getFirstName());
        assertEquals(responseGet.getLastName(), userDetails.getLastName());
        assertEquals(responseGet.getPhoneNumber(), userDetails.getPhoneNumber());
    }
}
