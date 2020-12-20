package nl.hro.cookbook.controller;

import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.RecipeDto;
import nl.hro.cookbook.model.dto.RecipeImageDTO;
import nl.hro.cookbook.security.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;

import static nl.hro.cookbook.controller.ImageHelper.createTempFileResource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerTest {

    @LocalServerPort
    private int port;

    private RecipeDto recipe;

    private User user;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void createRecipeResponse() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User(12L, "test1", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null));
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity response = restTemplate
                .postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        recipe = new RecipeDto(1L, "Test", "test", "Eat it raw!", user.getId(), new RecipeImageDTO());
        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test1", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + stringResponse.getBody());
        ResponseEntity<Long> response1 = restTemplate
                .withBasicAuth("test1", "test")
                .postForEntity(uri, request1, Long.class);
        assertThat(response1.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void updateRecipeResponse() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User(12L, "test", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null));
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity response = restTemplate
                .postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        recipe = new RecipeDto(1L, "Test", "test", "Eat it raw!", user.getId(), new RecipeImageDTO());

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        // create
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + stringResponse.getBody());
        ResponseEntity<Long> response1 = restTemplate
                .withBasicAuth("test", "test")
                .postForEntity(uri, request1, Long.class);
        System.out.println(response1.getBody());
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        // get
        uri = new URI("http://localhost:" + port + "/recipe/" + recipe.getId() + "/user/" + stringResponse.getBody());
        ResponseEntity<Recipe> recipeResponse = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity(uri,  Recipe.class);
        assertThat(recipeResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(recipeResponse.getBody()).getRecipe()).isEqualTo("test");

        // update
        uri = new URI("http://localhost:" + port + "/recipe/" + recipeResponse.getBody().getId() + "/user/" + stringResponse.getBody());
        System.out.println(stringResponse.getBody());
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body1
                = new LinkedMultiValueMap<>();
        recipe = new RecipeDto();
        recipe.setRecipe("Something with duck");
        body1.add("recipe", recipe);
        body1.add("file", null);
        request = new HttpEntity<>(body1, headers);
        restTemplate.withBasicAuth("test", "test").put(uri, request);

        ResponseEntity<Recipe> recipeResponse2 = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity(uri,  Recipe.class);
        assertThat(recipeResponse2.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(recipeResponse2.getBody()).getRecipe()).isEqualTo("Something with duck");
    }

}
