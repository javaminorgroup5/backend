package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes")
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String recipe;

    private String description;

    private Long userId;

    @Embedded
    RecipeImage recipeImage;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", recipe='" + recipe + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", recipeImage=" + recipeImage +
                '}';
    }
}
