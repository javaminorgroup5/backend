package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Category;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TestDataService testDataService;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long categoryId) throws ResourceNotFoundException {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(String.format("No category exists for id: %d", categoryId), Category.class));
    }

    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(final long categoryId, final Category updateCategory) {
        Category category = findCategoryById(categoryId);
        if (category == null || updateCategory == null) {
            return;
        }
        if (updateCategory.getCategoryName() != null && !updateCategory.getCategoryName().isEmpty()) {
            category.setCategoryName(updateCategory.getCategoryName());
        }
        if (updateCategory.getGroups() != null && !updateCategory.getGroups().isEmpty()) {
            category.setGroups(updateCategory.getGroups());
        }
        if (updateCategory.getActive() != null) {
            category.setActive(updateCategory.getActive());
        }
        categoryRepository.save(category);
    }

    @PostConstruct
    public void init() {
        categoryRepository.saveAll(testDataService.getCategories());
    }
}
