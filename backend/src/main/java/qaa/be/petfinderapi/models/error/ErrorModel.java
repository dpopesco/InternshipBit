package qaa.be.petfinderapi.models.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ErrorModel {
    //errors occurred on user endpoint
    @JsonProperty("Email")
    private ArrayList<String> email;
    @JsonProperty("LastName")
    private ArrayList<String> lastName;
    @JsonProperty("Password")
    private ArrayList<String> password;
    @JsonProperty("FirstName")
    private ArrayList<String> firstName;
    @JsonProperty("PhoneNumber")
    private ArrayList<String> phoneNumber;
    //errors occurred on post endpoint
    @JsonProperty("Area")
    private ArrayList<String> area;
    @JsonProperty("PetName")
    private ArrayList<String> petName;
    @JsonProperty("PetColor")
    private ArrayList<String> petColor;
    @JsonProperty("Description")
    private ArrayList<String> description;
    @JsonProperty("CoordinateLatitude")
    private ArrayList<String> coordinateLatitude;
    @JsonProperty("CoordinateLongitude")
    private ArrayList<String> coordinateLongitude;
}
