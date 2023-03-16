package qaa.be.dummyapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class ResponseStructure<Model> {

    @JsonProperty(value = "data")
    private List<Model> data;

    private int limit;
    private int total;
    private int page;
}
