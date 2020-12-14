package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hro.cookbook.model.domain.Profile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class GroupDTO {

    private long id;

    private String name;

    private String description;

    private Long userId;

    private Set<Profile> profiles;

}