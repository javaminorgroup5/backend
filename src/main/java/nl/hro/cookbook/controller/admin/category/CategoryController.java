package nl.hro.cookbook.controller.admin.category;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.controller.admin.AdminController;
import nl.hro.cookbook.model.domain.Category;
import nl.hro.cookbook.model.dto.CategoryDTO;
import nl.hro.cookbook.model.mapper.CategoryMapper;
import nl.hro.cookbook.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController extends AdminController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public Collection<CategoryDTO> getAllCategories() {
        return categoryService.findAllCategories().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody final CategoryDTO category) {
        Category categoryModel = categoryMapper.toModel(category);
        categoryService.saveCategory(categoryModel);
        return ResponseEntity.ok(categoryModel.getId());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editCategory(@PathVariable("id") final long id, @RequestBody final CategoryDTO category) {
        Category categoryModel = categoryMapper.toModel(category);
        categoryService.updateCategory(id, categoryModel);
        return ResponseEntity.ok(categoryModel.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable final long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
