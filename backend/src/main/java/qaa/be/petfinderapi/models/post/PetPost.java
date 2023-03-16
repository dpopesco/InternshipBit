package qaa.be.petfinderapi.models.post;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;


@Getter
@Setter
public class PetPost implements Serializable {

    String postId;

    String id;
    String petName;

    String petColor;
    String petGender;

    String coordinateLatitude;

    String coordinateLongitude;

    String area;

    String description;

    String userId;

    String petTypeId;

    String postStateId;

    String missingDate;

    String createdDate;

    Photo photo;

    String photoId;

    boolean hasPhoto;

    public PetPost() {
    }

    public static PetPost randomUpdatePostBody(String postId) {
        PetPost post = new PetPost();
        post.setId(postId);
        post.setPetName(randomAlphabetic(10));
        post.setCoordinateLongitude(randomNumeric(2));
        post.setCoordinateLatitude(randomNumeric(2));
        post.setArea(randomAlphabetic(7));
        post.setPetTypeId("1");
        post.setDescription(randomAlphabetic(7));

        return post;
    }

    public static Map<String, Object> randomPostBodyMap(String userId) throws IOException {
        URL url = new URL("https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F47%2F2022%2F06%2F03%2Fshiba-inu-sitting-on-rocks-35428903-2000.jpeg");
        String tDir = System.getProperty("java.io.tmpdir");
        String path = tDir + "tmp" + ".jpeg";
        File file = new File(path);
        file.deleteOnExit();
        FileUtils.copyURLToFile(url, file);
        HashMap<String, Object> newBody = new HashMap<>();
        newBody.put("petName", randomAlphabetic(5));
        newBody.put("petColor", randomAlphabetic(5));
        newBody.put("petGender", "1");
        newBody.put("coordinateLatitude", randomNumeric(2));
        newBody.put("coordinateLongitude", randomNumeric(2));
        newBody.put("area", randomAlphabetic(5));
        newBody.put("description", randomAlphabetic(5));
        newBody.put("userId", userId);
        newBody.put("petTypeId", "2");
        newBody.put("postStateId", "1");
        newBody.put("missingDate", "3/14/2023");
        newBody.put("formFile", file);

        return newBody;
    }
}
