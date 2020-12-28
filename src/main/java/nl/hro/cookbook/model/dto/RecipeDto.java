package nl.hro.cookbook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private Long id;
    private String title;
    private String recipe;
    private String description;
    private Long userId;
    private RecipeImageDTO recipeImageDTO;

}
