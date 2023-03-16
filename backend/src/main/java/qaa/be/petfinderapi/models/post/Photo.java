package qaa.be.petfinderapi.models.post;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Photo implements Serializable {

    String id;

    String name;

    String format;

    public Photo() {

    }

    public Photo(String id, String name, String format) {
        this.id = id;
        this.name = name;
        this.format = format;
    }
}
