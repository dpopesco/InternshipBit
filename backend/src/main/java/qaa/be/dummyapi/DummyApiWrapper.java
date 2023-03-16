package qaa.be.dummyapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import qaa.be.core.RestWrapper;
import qaa.be.dummyapi.requests.CommentsRequests;
import qaa.be.dummyapi.requests.PostsRequests;
import qaa.be.dummyapi.requests.TagsRequests;
import qaa.be.dummyapi.requests.UsersRequests;

@Slf4j
@Service
@Scope("prototype")
public class DummyApiWrapper extends RestWrapper<DummyApiWrapper> {

    public PostsRequests usingPosts() {
        log.info("Entering method where we are creating Post Request object!");
        return new PostsRequests(this);
    }

    public UsersRequests usingUsers() {
        return new UsersRequests(this);
    }

    public CommentsRequests usingComments() {
        log.info("Entering method where we are creating Post Request object!");
        return new CommentsRequests(this);
    }

    public TagsRequests usingTags() {
        log.info("Entering method where we are creating Post Request object!");
        return new TagsRequests(this);
    }
}
