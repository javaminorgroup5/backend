package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipeDto {

    private Long id;

    private String recipe;

    private String description;

    private String picture;

    private Long userId;

}
