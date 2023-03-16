package qaa.be.petfinderapi.models.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static org.testng.Assert.assertEquals;

@Getter
@Setter
public class DetailErrorModel {
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Status")
    private int status;
    @JsonProperty("Detail")
    private String detail;
    @JsonProperty("Instance")
    private Object instance;
    @JsonProperty("Extensions")
    private Object extensions;

    public DetailErrorModel hasErrorInDetail(String error) {
        assertEquals(getDetail(), error);
        return this;
    }
}
