package qaa.be.dummyapi.models.user;

import lombok.Getter;
import lombok.Setter;
import qaa.be.dummyapi.enums.Title;
import qaa.be.dummyapi.enums.Gender;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Getter
@Setter
public class UserModel {

    public static final String DOMAIN = java.lang.String.format("@%s.com",
            randomAlphabetic(5).toLowerCase());
    public static final String PICTURE_URL= "https://unsplash.com/photos/rDEOVtE7vOs";

    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    private String phone;
    private String picture;

    private String dateOfBirth;

    private String registerDate;

    private String updatedDate;

    private Location location;

    public UserModel() {

    }

    public UserModel(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public UserModel(String firstName, String lastName, String email, String gender, String title) {
        this(firstName, lastName, email);
        this.gender = gender;
        this.title = title;
    }

    public UserModel(String firstName, String lastName, String email, String gender, String title,
                     String phone, String picture, String dateOfBirth, Location location) {
        this(firstName, lastName, email, gender, title);
        this.phone = phone;
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.location = location;
    }

    public static UserModel generateRandomUser() {
        return new UserModel(
                randomAlphabetic(3, 29),
                randomAlphabetic(3, 29),
                randomAlphanumeric(3, 10).toLowerCase() + DOMAIN,
                Gender.MALE.getGenderType(),
                Title.MR.getTitleType());
    }

    public static UserModel generateRandomAllFieldsUser() {
        Random rand = new Random();
        UserModel newUser = generateRandomUser();
        newUser.setPhone(randomAlphabetic(5, 20));
        newUser.setPicture(PICTURE_URL);
        newUser.setDateOfBirth(OffsetDateTime.now(ZoneOffset.UTC).minusDays(rand.nextInt(100)).format
                (DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")));
        newUser.setLocation(Location.generateRandomLocation());

        return newUser;
    }
}
