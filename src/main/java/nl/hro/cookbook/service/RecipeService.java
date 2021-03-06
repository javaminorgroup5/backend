package nl.hro.cookbook.service;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import nl.hro.cookbook.model.domain.*;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.RecipeRepository;
import nl.hro.cookbook.repository.ShareLinkRepository;
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
    private final ShareLinkRepository shareLinkRepository;

    public List<Recipe> findRecipesByUserId(long userId) {
            return recipeRepository.findRecipesByUserId(userId).orElse(Collections.emptyList());
    }

    public List<Recipe> findRecipesByUserId(long userId, String prefix) {
        return recipeRepository.findRecipesByUserIdAndTitleContainingIgnoreCase(userId, prefix).orElse(Collections.emptyList());
    }

    public List<Recipe> findRecipesByGroupId(long groupId, String prefix) {
        return recipeRepository.findRecipesByGroupIdAndTitleContainingIgnoreCase(groupId, prefix).orElse(Collections.emptyList());
    }

    public List<Recipe> findRecipesByGroupId(long groupId) {
            return recipeRepository.findRecipesByGroupId(groupId).orElse(Collections.emptyList());
    }

    public Recipe findRecipeById(final long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No recipe exists for id: %d", recipeId), Recipe.class));
    }

    @Transactional
    public ShareLink generateShareLink(final long recipeId, final long userId) throws Exception {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No recipe exists for id: %d", recipeId), Recipe.class));

        if (recipe.getUserId() == userId) {
            ShareLink shareLink = new ShareLink(RandomString.make(12));
            List<ShareLink> shareLinks = recipe.getShareLinks();
            shareLinks.add(shareLink);
            recipe.setShareLinks(shareLinks);
            shareLinkRepository.save(shareLink);
            recipeRepository.save(recipe);
            return shareLink;
        } else {
            throw new Exception("You are not the owner of the group.");
        }
    }

    @Transactional
    public Recipe findRecipeByShareLink(final long recipeId, final String shareLink) throws NotFoundException {
        Optional<Recipe> recipeById = recipeRepository.findById(recipeId);
        if (recipeById.isPresent()) {
            Recipe recipe = recipeById.get();
            List<ShareLink> shareLinks = recipe.getShareLinks();
            for (ShareLink shareLinkIterated : shareLinks) {
                String shareLinkDB = shareLinkIterated.getShareLink();
                if (shareLinkDB.equals(shareLink)) {
                    return recipe;
                }
            }
        }
        throw new NotFoundException("Recipe not found");
    }

    @Transactional
    public void createRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Transactional
    public void updateRecipe(final long recipeId, final Recipe updateRecipe) {
        Recipe recipe = findRecipeById(recipeId);
        if (recipe == null || updateRecipe == null) {
            return;
        }
        if (updateRecipe.getTitle() != null && !updateRecipe.getTitle().isEmpty()) {
            recipe.setTitle(updateRecipe.getTitle());
        }
        if (updateRecipe.getRecipe() != null && !updateRecipe.getRecipe().isEmpty()) {
            recipe.setRecipe(updateRecipe.getRecipe());
        }
        if (updateRecipe.getDescription() != null && !updateRecipe.getDescription().isEmpty()) {
            recipe.setDescription(updateRecipe.getDescription());
        }
        if (updateRecipe.getImage() != null) {
            recipe.setImage(updateRecipe.getImage());
        }
        recipeRepository.save(recipe);
    }

}
