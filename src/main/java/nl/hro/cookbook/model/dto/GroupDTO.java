package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {

    private long id;
    private String name;
    private String description;
    private Long userId;
    private List<Profile> profiles;
    private GroupImageDTO groupImageDTO;
    private List<User> enrolledUsers;


