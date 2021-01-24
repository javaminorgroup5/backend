package nl.hro.cookbook.controller.admin;

import nl.hro.cookbook.model.domain.Category;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static nl.hro.cookbook.controller.TestHelper.createTempFileResource;
import static nl.hro.cookbook.controller.TestHelper.createHeaders;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminCategoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private User user;
    private final Category category = new Category();
    private final List<Group> groups = new ArrayList<>();

    @BeforeEach
    void setUp() {
        category.setGroups(groups);
        category.setCategoryName("test me");
    }

    @Test
    void createCategoryResponse() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User("test@email.com", "test", Role.ADMIN,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Category> request1 =
                new HttpEntity<>(category,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/admin/category");
        ResponseEntity<String> categoryIdResponse = restTemplate
                .withBasicAuth("test@email.com", "test")
                .postForEntity(uri1, request1, String.class);
        assertThat(categoryIdResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        String authStr = "test@email.com:test";
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request5 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/admin/category");
        ResponseEntity<List<Category>> likesResponse = restTemplate
                .exchange(uri, HttpMethod.GET, request5,  new ParameterizedTypeReference<List<Category>>() {});
        assertThat(likesResponse.getBody()).isNotEmpty();
        assertThat(Objects.requireNonNull(likesResponse.getBody()).size()).isGreaterThan(0);
    }

    @Test
    void createCategoryResponseWrongRole() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User("test39@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test39@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        body = new LinkedMultiValueMap<>();
        body.add("category", category);
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/admin/category");
        ResponseEntity<String> categoryIdResponse = restTemplate
                .withBasicAuth("test39@email.com", "test")
                .postForEntity(uri1, request1, String.class);
        assertThat(categoryIdResponse.getStatusCodeValue()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void updateCategoryResponse() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User("test44@email.com", "test", Role.ADMIN,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test44@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Category> request1 =
                new HttpEntity<>(category,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/admin/category");
        ResponseEntity<String> categoryIdResponse = restTemplate
                .withBasicAuth("test44@email.com", "test")
                .postForEntity(uri1, request1, String.class);
        assertThat(categoryIdResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        String authStr = "test44@email.com:test";
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request5 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/admin/category");
        ResponseEntity<List<Category>> likesResponse = restTemplate
                .exchange(uri, HttpMethod.GET, request5,  new ParameterizedTypeReference<List<Category>>() {});
        assertThat(likesResponse.getBody()).isNotEmpty();
        assertThat(Objects.requireNonNull(likesResponse.getBody()).size()).isGreaterThan(0);

        uri = new URI("http://localhost:" + port + "/admin/category/" + categoryIdResponse.getBody());
        headers = createHeaders("test44@email.com", "test");
        headers.setContentType(MediaType.APPLICATION_JSON);
        category.setActive(false);
        HttpEntity<Category> request2 =
                new HttpEntity<>(category,  headers);
        ResponseEntity<String> categoryIdResponse2 = restTemplate.exchange(uri, HttpMethod.PUT, request2, String.class);
        assertThat(categoryIdResponse2.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        request2 = new HttpEntity<>(null,  headers);
        uri = new URI("http://localhost:" + port + "/admin/category/" + categoryIdResponse.getBody());
        ResponseEntity<Category> categoryResponse = restTemplate.exchange(uri, HttpMethod.GET, request2, Category.class);
        assertThat(categoryResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(categoryResponse.getBody()).getActive()).isFalse();
    }
}
