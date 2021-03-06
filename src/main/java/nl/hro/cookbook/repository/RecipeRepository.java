package nl.hro.cookbook.repository;

import nl.hro.cookbook.model.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<List<Recipe>> findRecipesByUserId(Long userId);
    Optional<List<Recipe>> findRecipesByUserIdAndTitleContainingIgnoreCase(Long userId, String prefix);
    Optional<List<Recipe>> findRecipesByGroupId(Long groupId);
    Optional<List<Recipe>> findRecipesByGroupIdAndTitleContainingIgnoreCase(Long groupId, String prefix);
    Optional<Recipe> findRecipeById(Long recipeId);
}
