package qaa.be.dummyapi.models.error;

import lombok.Getter;
import lombok.Setter;

import static org.testng.Assert.assertEquals;

@Getter
@Setter
public class ErrorModel {
    private String error;

    public ErrorModel hasError(String error) {
        assertEquals(getError(), error, "Error if not valid!");
        return this;
    }
}
