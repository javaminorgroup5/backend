package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Category;
import nl.hro.cookbook.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

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

    @PostConstruct
    public void init() throws IOException {
        categoryRepository.saveAll(testDataService.getCategories());
    }
}
