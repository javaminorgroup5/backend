package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.RecipeDto;
import nl.hro.cookbook.model.mapper.RecipeMapper;
import nl.hro.cookbook.service.RecipeService;
import nl.hro.cookbook.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/recipe"}, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final RecipeMapper recipeMapper;

    @GetMapping()
    public Collection<RecipeDto> getAllRecipes() {
        return recipeService.findAllRecipe().stream()
                .map(recipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/create/{user_id}")
    public ResponseEntity createRecipe(@PathVariable("user_id") final long userId, @RequestBody Recipe recipe) {
        User user = userService.findUserById(userId);
        recipe.setUserId(user.getId());
        recipeService.createRecipe(recipe);
        return ResponseEntity.badRequest().body(recipe.getId());
    }

    @GetMapping("/{recipe_id}/user/{user_id}")
    public ResponseEntity getRecipe(@PathVariable("recipe_id") final long recipeId, @PathVariable("user_id") final long userId) {
        User user = userService.findUserById(userId);
        RecipeDto recipe = recipeMapper.toDTO(recipeService.findRecipeById(recipeId));
        if (user.getId() == recipe.getUserId()) {
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    // TODO
    @PutMapping("/{recipe_id}/user/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfRecipe(@PathVariable("recipe_id") final long recipeId, @PathVariable("user_id") final long userId, @RequestBody Recipe recipe) {
        recipeService.updateRecipe(userId, recipe);
    }

}