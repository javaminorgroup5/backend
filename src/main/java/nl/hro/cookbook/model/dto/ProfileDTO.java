package nl.hro.cookbook.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@NoArgsConstructor
public class ProfileDTO {

    private String id;
    private String username;
    //placeholder type of String until we figure out how we're going to handle images.
    private String profilePicture;


    public ProfileDTO(String id, String username, String profilePicture) {

        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }

}
