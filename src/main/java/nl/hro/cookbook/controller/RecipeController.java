package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.domain.RecipeImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.RecipeDto;
import nl.hro.cookbook.model.mapper.RecipeMapper;

import nl.hro.cookbook.service.CommonService;
import nl.hro.cookbook.service.RecipeService;
import nl.hro.cookbook.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/recipe"}, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final RecipeMapper recipeMapper;
    private final CommonService commonService;

    @GetMapping("/{user_id}")
    public Collection<Recipe> getAllByUserIdRecipes(@PathVariable("user_id") final long userId) {
        Collection<Recipe> recipes = recipeService.findRecipesByUserId(userId);
        for (Recipe recipe : recipes) {
            recipe.getRecipeImage().setPicByte(commonService.decompressBytes(recipe.getRecipeImage().getPicByte()));
        }
        return recipes;
    }

    @PostMapping(value = "/create/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createRecipe(@PathVariable("user_id") final long userId,
                                       @RequestPart("recipe") RecipeDto recipeDTO,
                                       @RequestPart("file") MultipartFile file) throws IOException {
        User user = userService.findUserById(userId);
        Recipe recipe = recipeMapper.toModel(recipeDTO);
        RecipeImage recipeImage = new RecipeImage(file.getOriginalFilename(), file.getName(),
                        commonService.compressBytes(file.getBytes()));
        recipe.setRecipeImage(recipeImage);
        recipe.setUserId(user.getId());
        recipeService.createRecipe(recipe);
        return ResponseEntity.ok(recipe.getId());
    }

    @GetMapping("/{recipe_id}/user/{user_id}")
    public ResponseEntity getRecipe(@PathVariable("recipe_id") final long recipeId, @PathVariable("user_id") final long userId) {
        User user = userService.findUserById(userId);
        Recipe recipe = recipeService.findRecipeById(recipeId);
        recipe.getRecipeImage().setPicByte(commonService.decompressBytes(recipe.getRecipeImage().getPicByte()));
        if (user.getId() == recipe.getUserId()) {
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{recipe_id}/user/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateRecipe(@PathVariable("recipe_id") final long recipeId,
                                       @PathVariable("user_id") final long userId,
                                       @RequestPart(value = "recipe", required = false) RecipeDto recipeDto,
                                       @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        userService.findUserById(userId);
        Recipe recipe = null;
        if(recipeDto != null) {
            recipe = recipeMapper.toModel(recipeDto);
        }
        if (file != null && recipe != null) {
            RecipeImage recipeImage = new RecipeImage(file.getOriginalFilename(), file.getName(),
                    commonService.compressBytes(file.getBytes()));
            recipe.setRecipeImage(recipeImage);
        }
        recipeService.updateRecipe(recipeId, recipe);
    }

    @DeleteMapping("/{recipe_id}/user/{user_id}")
    public ResponseEntity deleteRecipe(@PathVariable("recipe_id") final long recipeId, @PathVariable("user_id") final long userId) {
        User user = userService.findUserById(userId);
        Recipe recipe = recipeService.findRecipeById(recipeId);
        if (user.getId() == recipe.getUserId()) {
            recipeService.deleteById(recipeId);
            return ResponseEntity.ok("recipe deleted");
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }
}