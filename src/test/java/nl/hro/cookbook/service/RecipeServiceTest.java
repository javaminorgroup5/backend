package nl.hro.cookbook.service;

import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @InjectMocks
    private RecipeService recipeServiceTest;

    @Test
    public void createRecipeTest() {
        // Give
        Recipe recipe = new Recipe();
        recipe.setRecipe("Pasta");
        recipe.setDescription("Italian dish");
        recipe.setPicture("picture");
        recipe.setUserId(1L);
        lenient().when(recipeRepository.findRecipesByUserId(eq(1L))).thenReturn(Optional.of(Collections.singletonList(recipe)));

        // When
        recipeServiceTest.createRecipe(recipe);

        // Then
        Optional<List<Recipe>> optionalRecipeList = recipeRepository.findRecipesByUserId(1L);
        assertTrue(optionalRecipeList.isPresent());
        assertEquals(recipe, optionalRecipeList.get().get(0));
    }

}
