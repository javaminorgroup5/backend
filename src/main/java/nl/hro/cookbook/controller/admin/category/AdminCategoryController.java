package nl.hro.cookbook.controller.admin.category;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Category;
import nl.hro.cookbook.model.dto.CategoryDTO;
import nl.hro.cookbook.model.mapper.CategoryMapper;
import nl.hro.cookbook.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public Collection<CategoryDTO> getAllCategories() {
        return categoryService.findAllCategories().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@RequestBody final CategoryDTO category) {
        Category categoryModel = categoryMapper.toModel(category);
        categoryService.saveCategory(categoryModel);
        return ResponseEntity.ok(categoryModel.getId());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editCategory(@PathVariable("id") final long id, @RequestBody final CategoryDTO category) {
        Category categoryModel = categoryMapper.toModel(category);
        categoryService.updateCategory(id, categoryModel);
        return ResponseEntity.ok(categoryModel.getId());
    }

    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCategory(@PathVariable("id") final long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }
}
