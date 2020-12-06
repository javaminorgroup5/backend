package nl.hro.cookbook.controller;

import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.security.Role;
import org.junit.jupiter.api.BeforeEach;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private User user;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() throws URISyntaxException {
        user = new User(12L, "test", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", "test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request =
                new HttpEntity<>(user, headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<User> response = restTemplate
                .postForEntity(uri, request, User.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void createUserResponse() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        user.setId(44);
        user.setUsername("test2");
        HttpEntity<User> request =
                new HttpEntity<>(user, headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<User> response = restTemplate
                .postForEntity(uri, request, User.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

//    @Test
    void getProfileResponse() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        uri = new URI("http://localhost:" + port + "/users/2/profile");
        ResponseEntity<Profile> profileResponse = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity(uri,  Profile.class);
        assertThat(profileResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(profileResponse.getBody()).getProfileName()).isEqualTo("Top Gun");
    }

}
