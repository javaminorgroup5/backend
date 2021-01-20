package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Category;
import nl.hro.cookbook.model.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends ModelToDTOMapper<Category, CategoryDTO> {}