package nl.hro.cookbook.service;

import nl.hro.cookbook.model.domain.Category;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private final Category category = new Category();
    private final List<Group> groups = new ArrayList<>();

    @BeforeEach
    void setup() {
        category.setGroups(groups);
        category.setCategoryName("test me");
    }

    @Test
    void testFindCategory() {
        // Given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        // When
        Category categoryById = categoryService.findCategoryById(1L);

        // Then
        assertThat(categoryById).isNotNull();
        assertEquals(categoryById.getCategoryName(), "test me");
    }

    @Test
    void testUpdateCategory() {
        // Given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        // When
        Category categoryById = categoryService.findCategoryById(1L);
        categoryById.setCategoryName("I have changed");
        categoryService.updateCategory(1L, categoryById);

        // Then
        assertThat(categoryById).isNotNull();
        assertEquals(categoryById.getCategoryName(), "I have changed");
    }
}
