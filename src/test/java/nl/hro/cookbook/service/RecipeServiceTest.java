package nl.hro.cookbook.service;

import javassist.NotFoundException;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @InjectMocks
    private RecipeService recipeServiceTest;

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setRecipe("Pasta");
        recipe.setDescription("Italian dish");
        recipe.setUserId(1L);
    }

    @Test
    void createRecipeTest() {
        // Given
        when(recipeRepository.findRecipesByUserId(eq(1L))).thenReturn(Optional.of(Collections.singletonList(recipe)));

        // When
        recipeServiceTest.createRecipe(recipe);

        // Then
        Optional<List<Recipe>> optionalRecipeList = recipeRepository.findRecipesByUserId(1L);
        assertTrue(optionalRecipeList.isPresent());
        assertEquals(recipe, optionalRecipeList.get().get(0));
    }

    @Test
    void updateRecipeTest() throws NotFoundException {
        // Give
        when(recipeRepository.findById(eq(1L))).thenReturn(Optional.of((recipe)));

        // When
        recipeServiceTest.createRecipe(recipe);
        recipe.setRecipe("Sushi");
        recipe.setDescription("Japanese dish");
        recipeServiceTest.updateRecipe(recipe.getId(), recipe);

        // Then
        Optional<Recipe> optionalRecipe = recipeRepository.findById(1L);
        assertTrue(optionalRecipe.isPresent());
        assertEquals(recipe, optionalRecipe.get());
    }

}
