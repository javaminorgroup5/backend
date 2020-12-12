package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class RecipeDto {

    private Long id;
    private String recipe;
    private String description;
    private Long userId;
    private RecipeImageDTO recipeImageDTO;

}
