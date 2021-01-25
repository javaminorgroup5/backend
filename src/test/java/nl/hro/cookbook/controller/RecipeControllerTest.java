package nl.hro.cookbook.controller;

import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Image;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.RecipeDto;
import nl.hro.cookbook.model.dto.ImageDTO;
import nl.hro.cookbook.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static nl.hro.cookbook.controller.TestHelper.createTempFileResource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerTest {

    @LocalServerPort
    private int port;

    private RecipeDto recipe;

    private User user;

    @Autowired
    private TestRestTemplate restTemplate;

    private List<Group> groups;

    @BeforeEach
    void setUp() {
        Image image = new Image("group.jpg", "file", new byte[12]);
        final Group initialGroup1 = new Group("PastaGroep", "Leuke pasta groep", 12L, null, Group.GroupPrivacy.INVITE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup1.setId(1L);
        final Group initialGroup2 = new Group("RodeSauzen", "Roder dan rood", 12L, null, Group.GroupPrivacy.INVITE,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup2.setId(2L);
        final Group initialGroup3 = new Group("Bloemkoollovers", "Bloemkool is een groente die hoort bij het geslacht kool uit de kruisbloemenfamilie (Brassicaceae). De botanische naam voor bloemkool is Brassica oleracea convar. ", 12L, null, Group.GroupPrivacy.INVITE,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup3.setId(3L);
        groups = Arrays.asList(initialGroup1, initialGroup2, initialGroup3);
    }

//    @Test
    void createRecipeResponse() throws Exception {
        // create user
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User( "test1@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<?> response = restTemplate
                .postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        // login
        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> idResponse = restTemplate
                .withBasicAuth("test1@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(idResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + idResponse.getBody());
        ResponseEntity<?> response1 = restTemplate
                .withBasicAuth("test1@email.com", "test")
                .postForEntity(uri, request1, Void.class);
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
        user = new User("test4@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<String> userIdResponse = restTemplate
                .postForEntity(uri, request, String.class);
        assertThat(userIdResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        recipe = new RecipeDto(1L, "Test", "test", "Eat it raw!", user.getId(), 1L, new ImageDTO(), new ArrayList<>());

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        body.add("group", groups.get(0));
        body.add("groupCategoryId", 1);
        HttpEntity<MultiValueMap<String, Object>> groupRequest =
                new HttpEntity<>(body,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/group/create/" + userIdResponse.getBody());
        ResponseEntity<Long> groupIdResponse = restTemplate
                .withBasicAuth("test4@email.com", "test")
                .postForEntity(uri1, groupRequest, Long.class);
        assertThat(groupIdResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(groupIdResponse.getBody()).isNotNull();

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test4@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        recipe.setGroupId(groupIdResponse.getBody());
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + stringResponse.getBody());
        ResponseEntity<Long> response1 = restTemplate
                .withBasicAuth("test4@email.com", "test")
                .postForEntity(uri, request1, Long.class);
        assertThat(response1.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response1.getBody()).isNotNull();

        uri = new URI("http://localhost:" + port + "/recipe/" + response1.getBody() + "/user/" + stringResponse.getBody()+"?q=test");
        ResponseEntity<Recipe> recipeResponse = restTemplate
                .withBasicAuth("test4@email.com", "test")
                .getForEntity(uri,  Recipe.class);
        assertThat(recipeResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(recipeResponse.getBody()).getRecipe()).isEqualTo("test");

        uri = new URI("http://localhost:" + port + "/recipe/" + recipeResponse.getBody().getId() + "/user/" + stringResponse.getBody());
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
        restTemplate.withBasicAuth("test4@email.com", "test").put(uri, request);

        ResponseEntity<Recipe> recipeResponse2 = restTemplate
                .withBasicAuth("test4@email.com", "test")
                .getForEntity(uri,  Recipe.class);
        assertThat(recipeResponse2.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(recipeResponse2.getBody()).getRecipe()).isEqualTo("Something with duck");
    }

    @Test
    void getListOfRecipes() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User("test5@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<?> response = restTemplate
                .postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> userIdResponse = restTemplate
                .withBasicAuth("test5@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(userIdResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        body.add("group", groups.get(0));
        body.add("groupCategoryId", 1);
        HttpEntity<MultiValueMap<String, Object>> groupRequest =
                new HttpEntity<>(body,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/group/create/" + userIdResponse.getBody());
        ResponseEntity<Long> groupIdResponse = restTemplate
                .withBasicAuth("test5@email.com", "test")
                .postForEntity(uri1, groupRequest, Long.class);
        assertThat(groupIdResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(groupIdResponse.getBody()).isNotNull();

        recipe = new RecipeDto(1L, "Test", "test", "Eat it raw!", user.getId(), groupIdResponse.getBody(), new ImageDTO(), new ArrayList<>());
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        recipe.setTitle("first");
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + userIdResponse.getBody());
        ResponseEntity<Long> response1 = restTemplate
                .withBasicAuth("test5@email.com", "test")
                .postForEntity(uri, request1, Long.class);
        assertThat(response1.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response1.getBody()).isNotNull();

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        recipe.setTitle("second");
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request2 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + userIdResponse.getBody());
        ResponseEntity<Long> response2 = restTemplate
                .withBasicAuth("test5@email.com", "test")
                .postForEntity(uri, request2, Long.class);
        assertThat(response2.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.getBody()).isNotNull();

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        recipe.setTitle("third");
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request3 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + userIdResponse.getBody());
        ResponseEntity<Long> response3 = restTemplate
                .withBasicAuth("test5@email.com", "test")
                .postForEntity(uri, request3, Long.class);
        assertThat(response3.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response3.getBody()).isNotNull();

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        recipe.setTitle("third_plus");
        body.add("recipe", recipe);
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> request4 =
                new HttpEntity<>(body,  headers);
        uri = new URI("http://localhost:" + port + "/recipe/create/" + userIdResponse.getBody());
        ResponseEntity<Long> response4 = restTemplate
                .withBasicAuth("test5@email.com", "test")
                .postForEntity(uri, request4, Long.class);
        assertThat(response4.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response4.getBody()).isNotNull();


        uri = new URI("http://localhost:" + port + "/recipe/" + response1.getBody() + "/user/" + userIdResponse.getBody()+"?q=test");
        ResponseEntity<Recipe> recipeResponse = restTemplate
                .withBasicAuth("test5@email.com", "test")
                .getForEntity(uri,  Recipe.class);
        assertThat(recipeResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(recipeResponse.getBody()).getRecipe()).isEqualTo("test");

        String authStr = "test5@email.com:test";
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request5 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/recipe/" + userIdResponse.getBody());
        ResponseEntity<List<Recipe>> recipesResponse = restTemplate
                .exchange(uri, HttpMethod.GET, request5,  new ParameterizedTypeReference<List<Recipe>>() {});
        assertThat(recipesResponse.getBody());
        assertThat(Objects.requireNonNull(recipesResponse.getBody()).size()).isEqualTo(4);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request6 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/recipe/" + userIdResponse.getBody() + "?q=first");
        ResponseEntity<List<Recipe>> recipesResponse1 = restTemplate
                .exchange(uri, HttpMethod.GET, request6,  new ParameterizedTypeReference<List<Recipe>>() {});
        assertThat(recipesResponse1.getBody());
        assertThat(Objects.requireNonNull(recipesResponse1.getBody()).size()).isEqualTo(1);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request7 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/recipe/" + userIdResponse.getBody() + "?q=third");
        ResponseEntity<List<Recipe>> recipesResponse2 = restTemplate
                .exchange(uri, HttpMethod.GET, request7,  new ParameterizedTypeReference<List<Recipe>>() {});
        assertThat(recipesResponse2.getBody());
        assertThat(Objects.requireNonNull(recipesResponse2.getBody()).size()).isEqualTo(2);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request8 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/recipe/" + userIdResponse.getBody() + "?q=''");
        ResponseEntity<List<Recipe>> recipesResponse3 = restTemplate
                .exchange(uri, HttpMethod.GET, request8,  new ParameterizedTypeReference<List<Recipe>>() {});
        assertThat(recipesResponse3.getBody());
        assertThat(Objects.requireNonNull(recipesResponse3.getBody()).size()).isEqualTo(0);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request9 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/recipe/" + userIdResponse.getBody() + "?q=");
        ResponseEntity<List<Recipe>> recipesResponse4 = restTemplate
                .exchange(uri, HttpMethod.GET, request9,  new ParameterizedTypeReference<List<Recipe>>() {});
        assertThat(recipesResponse4.getBody());
        assertThat(Objects.requireNonNull(recipesResponse4.getBody()).size()).isEqualTo(4);
    }

}
