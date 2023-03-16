package qaa.be.dummyapi.models.error;

import lombok.Getter;
import lombok.Setter;
import qaa.be.dummyapi.models.post.PostPOST;

@Getter
@Setter
public class PostErrorModel extends ErrorModel {
    private PostPOST data;
}
