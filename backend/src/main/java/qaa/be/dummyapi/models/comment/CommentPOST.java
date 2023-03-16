package qaa.be.dummyapi.models.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Getter
@Setter
public class CommentPOST extends Comment {
    @JsonProperty(value = "owner", required = true)
    private String ownerId;

    public CommentPOST() {
    }

    public CommentPOST(String message, String ownerId, String postId) {
        setMessage(message);
        setOwnerId(ownerId);
        setPostId(postId);
    }

    public static CommentPOST generateComment(String ownerId, String postId) {
        String msg = randomAlphanumeric(30);
        return new CommentPOST(msg, ownerId, postId);
    }
}
