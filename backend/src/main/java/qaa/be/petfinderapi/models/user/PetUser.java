package qaa.be.petfinderapi.models.user;

import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.RandomStringUtils.*;

@Getter
@Setter
public class PetUser {

    public static final String DOMAIN = String.format("@%s.com",
            randomAlphabetic(5).toLowerCase());

    private String token;
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    private String phoneNumber;

    public PetUser() {

    }

    public PetUser(String firstName, String lastName, String password, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static PetUser generateRandomUser() {
        return new PetUser(
                randomAlphabetic(2, 256),
                randomAlphabetic(2, 256),
                randomAlphabetic(4).toLowerCase() + "A5*",
                randomAlphanumeric(4).toLowerCase() + DOMAIN,
                randomNumeric(5));
    }
}
