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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/recipe"}, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final RecipeMapper recipeMapper;
    private final CommonService commonService;

    @GetMapping()
    public Collection<RecipeDto> getAllRecipes() {
        return recipeService.findAllRecipe().stream()
                .map(recipeMapper::toDTO)
                .collect(Collectors.toList());
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
        RecipeDto recipe = recipeMapper.toDTO(recipeService.findRecipeById(recipeId));
        if (user.getId() == recipe.getUserId()) {
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

//    @PutMapping("/{recipe_id}/user/{user_id}")
//    public ResponseEntity updateProfRecipe(@PathVariable("recipe_id") final long recipeId, @PathVariable("user_id") final long userId, @RequestBody Recipe recipe) {
//        User user = userService.findUserById(userId);
//        if (user.getId() == recipe.getUserId()) {
//            return ResponseEntity.ok(recipe);
//        }
//        recipeService.updateRecipe(recipeId, recipe);
//        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
//    }

//    @PostMapping("/image/upload")
//    public ResponseEntity uplaodImage(@RequestParam("imageFile") MultipartFile file, @PathVariable("recipe_id") final long recipeId) throws IOException {
//        System.out.println("Original RecipeImage Byte Size - " + file.getBytes().length);
//        RecipeImage img = new RecipeImage(file.getOriginalFilename(), file.getContentType(),
//                commonService.compressBytes(file.getBytes()), recipeId);
//        imageRepository.save(img);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

//    @GetMapping(path = { "/image/get/{imageName}" })
//    public ResponseEntity getImage(@PathVariable("imageName") String imageName, @PathVariable("recipe_id") final long recipeId) {
//        final Optional<RecipeImage> retrievedImage = imageRepository.findByName(imageName);
//        if (!retrievedImage.isPresent()) {
//            return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
//        }
//        RecipeImage img = new RecipeImage(retrievedImage.get().getName(), retrievedImage.get().getType(),
//                commonService.decompressBytes(retrievedImage.get().getPicByte()), recipeId);
//        return ResponseEntity.ok(img);
//    }
}