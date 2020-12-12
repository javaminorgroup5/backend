package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Collection<Recipe> findAllRecipe() {
        return recipeRepository.findAll();
    }

    public Recipe findRecipeById(final long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No recipe exists for id: %d", recipeId), Recipe.class));
    }

    @Transactional()
    public void createRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public Optional<List<Recipe>> findRecipesByUserId(Long userId) {
        return recipeRepository.findRecipesByUserId(userId);
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