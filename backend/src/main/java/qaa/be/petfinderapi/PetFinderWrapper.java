package qaa.be.petfinderapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import qaa.be.core.RestWrapper;
import qaa.be.petfinderapi.requests.PetTypesRequests;
import qaa.be.petfinderapi.requests.PostsRequests;
import qaa.be.petfinderapi.requests.UsersRequests;

@Slf4j
@Service
@Scope("prototype")
public class PetFinderWrapper extends RestWrapper<PetFinderWrapper> {

    public PostsRequests usingPosts() {
        log.info("Entering method where we are creating Post Request object!");
        return new PostsRequests(this);
    }

    public UsersRequests usingUsers() {
        log.info("Entering method where we are creating User Request object!");
        return new UsersRequests(this);
    }

    public PetTypesRequests usingPetTypes() {
        log.info("Entering method where we are creating PetTypes Request object!");
        return new PetTypesRequests(this);
    }
}
