package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> findRecipesByUserId(long userId) {
            return recipeRepository.findRecipesByUserId(userId).orElse(Collections.emptyList());
    }

    public Recipe findRecipeById(final long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No recipe exists for id: %d", recipeId), Recipe.class));
    }

    @Transactional()
    public void createRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Transactional()
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Transactional()
    public void updateRecipe(final long recipeId, final Recipe updateRecipe) {
        Recipe recipe = findRecipeById(recipeId);
        if (!updateRecipe.getRecipe().isEmpty()) {
            recipe.setRecipe(updateRecipe.getRecipe());
        }
        if (!updateRecipe.getDescription().isEmpty()) {
            recipe.setDescription(updateRecipe.getDescription());
        }
        if (updateRecipe.getRecipeImage() != null) {
            recipe.setRecipeImage(updateRecipe.getRecipeImage());
        }
        recipeRepository.save(recipe);
    }

}