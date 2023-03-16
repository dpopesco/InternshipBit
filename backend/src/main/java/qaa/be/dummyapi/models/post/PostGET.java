package qaa.be.dummyapi.models.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import qaa.be.dummyapi.models.user.UserModel;

@Getter
@Setter
public class PostGET extends Post {

    @JsonProperty(value = "owner")
    private UserModel user;


}
