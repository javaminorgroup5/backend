package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Invite;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;

import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDTO {

    private long id;
    private Boolean active;
    private String categoryName;
    private List<Group> groups;
}
