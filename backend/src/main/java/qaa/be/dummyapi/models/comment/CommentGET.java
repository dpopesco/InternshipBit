package qaa.be.dummyapi.models.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import qaa.be.dummyapi.models.user.UserModel;

import java.util.Date;

@Getter
@Setter
public class CommentGET extends Comment {
    @JsonProperty(value = "owner")
    private UserModel owner;
    @JsonProperty(value = "publishDate")
    private Date publishDate;
}
