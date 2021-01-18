package nl.hro.cookbook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import nl.hro.cookbook.model.domain.Invite;
=======
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
import nl.hro.cookbook.model.domain.ShareLink;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private Long id;
    private String title;
    private String recipe;
    private String description;
    private Long userId;
<<<<<<< HEAD
    private RecipeImageDTO recipeImageDTO;
=======
    private ImageDTO recipeImageDTO;
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
    private List<ShareLink> shareLinks;

}
