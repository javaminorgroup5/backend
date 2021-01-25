package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hro.cookbook.model.domain.*;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {

    private long id;
    private String groupName;
    private String description;
    private Category category;
    private Long userId;
    private Group.GroupPrivacy groupPrivacy;
    private List<Profile> profiles;
    private ImageDTO image;
    private List<User> enrolledUsers;
    private List<Invite> invites;

}
