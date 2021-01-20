package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Invite;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDTO {

    private long id;
    private String categoryName;
}
