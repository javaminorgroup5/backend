package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.dto.RecipeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeMapper extends ModelToDTOMapper<Recipe, RecipeDto> {}