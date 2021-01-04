package nl.hro.cookbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;
import java.util.List;

class TestDataServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    TestDataService testDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUsers() throws IOException {
        List<User> result = testDataService.getUsers();
        assertEquals(1L, result.get(0).getId());
        assertEquals("dion", result.get(0).getUsername());
    }

    @Test
    void testGetRecipes() throws IOException {
        List<Recipe> result = testDataService.getRecipes();
        assertEquals("Budget recept: tomatenrijst met kip", result.get(0).getTitle());
    }
}
