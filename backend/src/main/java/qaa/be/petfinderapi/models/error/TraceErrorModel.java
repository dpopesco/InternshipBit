package qaa.be.petfinderapi.models.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

@Getter
@Setter
public class TraceErrorModel {
    @JsonProperty("type")
    private String type;
    @JsonProperty("title")
    private String title;
    @JsonProperty("status")
    private int status;
    @JsonProperty("traceId")
    private String traceId;
    @JsonProperty("errors")
    private ErrorModel errors;

    public TraceErrorModel hasError(ArrayList<String> error) {
        assertEquals(getErrors(), error, "Error if not valid!");
        return this;
    }
}
