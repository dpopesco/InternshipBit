package qaa.be.petfinderapi.models.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilteredPosts {

    int totalPostsCount;

    List<PetPost> posts;

    public FilteredPosts() {
    }

    public FilteredPosts(int totalPostsCount, List<PetPost> posts) {
        this.totalPostsCount = totalPostsCount;
        this.posts = posts;
    }
}
