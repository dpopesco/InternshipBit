package qaa.be.dummyapi.models.error;

import lombok.Getter;
import lombok.Setter;
import qaa.be.dummyapi.models.user.UserModel;

@Getter
@Setter
public class UserErrorModel extends ErrorModel {
    private UserModel data;
}
