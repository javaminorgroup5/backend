package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipeDto {

    private Long id;
    private String title;
    private String recipe;
    private String description;
    private Long userId;
    private String recipeImage;

}
