package qaa.be.test.petfinderapi.post;

import org.testng.annotations.Test;
import qaa.be.petfinderapi.models.post.FilteredPosts;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class GetAllPostByPostStateIdAndPetTypes extends PostBase {

    @Test
    public void getAllPostByPetTypesAndPostStateId() {
        FilteredPosts responseAllPosts = restWrapper.usingPosts()
                .usingParams("Skip=0", "Limit=5")
                .getAllPostBy("1", "2");

        assertEquals(restWrapper.getStatusCode(), SC_OK);
        assertEquals(responseAllPosts.getPosts().size(), 5);
    }
}
