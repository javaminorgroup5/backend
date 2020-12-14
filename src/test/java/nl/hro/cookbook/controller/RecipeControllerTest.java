//package nl.hro.cookbook.controller;
//
//import nl.hro.cookbook.model.domain.Profile;
//import nl.hro.cookbook.model.domain.Recipe;
//import nl.hro.cookbook.model.domain.RecipeImage;
//import nl.hro.cookbook.model.domain.User;
//import nl.hro.cookbook.security.Role;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//
//import java.io.File;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Objects;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class RecipeControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    private Recipe recipe;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @BeforeEach
//    void setUp() throws URISyntaxException {
//        User user = new User(12L, "test", "test", Role.COMMUNITY_MANAGER,
//                new Profile("Top Gun", "test.png"));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<User> request =
//                new HttpEntity<>(user, headers);
//        URI uri = new URI("http://localhost:" + port + "/users/create");
//        ResponseEntity<User> response = restTemplate
//                .postForEntity(uri, request, User.class);
//        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//        recipe = new Recipe(1L, "Test", "test", user.getId(), new RecipeImage());
//    }
//
//    @Test
//    void createRecipeResponse() throws Exception {
//        URI uri = new URI("http://localhost:" + port + "/users/login");
//        ResponseEntity<String> stringResponse = restTemplate
//                .withBasicAuth("test", "test")
//                .getForEntity(uri,  String.class);
//        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Recipe> request =
//                new HttpEntity<>(recipe, headers);
//        uri = new URI("http://localhost:" + port + "/recipe/create/" + stringResponse.getBody());
//        ResponseEntity<Long> response = restTemplate
//                .withBasicAuth("test", "test")
//                .postForEntity(uri, request, Long.class);
//        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//    }
//
////    @Test
//    void updateRecipeResponse() throws Exception {
//        URI uri = new URI("http://localhost:" + port + "/users/login");
//        ResponseEntity<String> stringResponse = restTemplate
//                .withBasicAuth("test", "test")
//                .getForEntity(uri,  String.class);
//        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Recipe> request =
//                new HttpEntity<>(recipe, headers);
//        uri = new URI("http://localhost:" + port + "/recipe/create/" + stringResponse.getBody());
//        ResponseEntity<Long> response = restTemplate
//                .withBasicAuth("test", "test")
//                .postForEntity(uri, request, Long.class);
//        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        uri = new URI("http://localhost:" + port + "/recipe/" + recipe.getId() + "/user/" + stringResponse.getBody());
//        ResponseEntity<Recipe> recipeResponse = restTemplate
//                .withBasicAuth("test", "test")
//                .getForEntity(uri,  Recipe.class);
//        assertThat(recipeResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//        assertThat(Objects.requireNonNull(recipeResponse.getBody()).getRecipe()).isEqualTo("Test");
//
//        recipe.setRecipe("Something with duck");
//        request = new HttpEntity<>(recipe, headers);
//        ResponseEntity<String> response1 = restTemplate
//                .withBasicAuth("test", "test")
//                .exchange(uri, HttpMethod.PUT, request, String.class);
//        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        ResponseEntity<Recipe> recipeResponse2 = restTemplate
//                .withBasicAuth("test", "test")
//                .getForEntity(uri,  Recipe.class);
//        assertThat(recipeResponse2.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//        assertThat(Objects.requireNonNull(recipeResponse2.getBody()).getRecipe()).isEqualTo("Something with duck");
//    }
//
//}
