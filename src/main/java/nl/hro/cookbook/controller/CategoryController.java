package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.dto.CategoryDTO;
import nl.hro.cookbook.model.mapper.CategoryMapper;
import nl.hro.cookbook.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/category"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping()
    public Collection<CategoryDTO> getAllCategories() {
        return categoryService.findAllCategories().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
