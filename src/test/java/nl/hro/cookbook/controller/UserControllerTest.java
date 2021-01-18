package nl.hro.cookbook.controller;

import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.security.Role;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.net.URI;
import java.nio.charset.Charset;
<<<<<<< HEAD
=======
import java.nio.charset.StandardCharsets;
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static nl.hro.cookbook.controller.ImageHelper.createTempFileResource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private User user;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createUserTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User(12L, "test1@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity response = restTemplate
                .postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

//    @Test
    public void getProfileTest() throws Exception {
        // create user
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User(12L, "test2@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity response = restTemplate
                .postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        // login
        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> idResponse = restTemplate
                .withBasicAuth("test2@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(idResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        // get profile
        uri = new URI("http://localhost:" + port + "/users/" + idResponse.getBody() +"/profile");
        ResponseEntity<Profile> profileResponse = restTemplate
                .withBasicAuth("test2@email.com", "test")
                .getForEntity(uri,  Profile.class);
        assertThat(profileResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(profileResponse.getBody()).getProfileName()).isEqualTo("Top Gun");
    }

    @Test
    public void updateProfileTest() throws Exception {
        // create user
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        user = new User(12L, "test3@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity response = restTemplate
                .postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        // login
        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> idResponse = restTemplate
                .withBasicAuth("test3@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(idResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        // get profile
        uri = new URI("http://localhost:" + port + "/users/" + idResponse.getBody() +"/profile");
        ResponseEntity<Profile> profileResponse = restTemplate
                .withBasicAuth("test3@email.com", "test")
                .getForEntity(uri,  Profile.class);
        assertThat(profileResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(profileResponse.getBody()).getProfileName()).isEqualTo("Top Gun");

        // update profile
        uri = new URI("http://localhost:" + port + "/users/" + idResponse.getBody() + "/profile");
        headers = createHeaders("test3@email.com", "test");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Profile profile = new Profile();
        profile.setProfileName("Maverick");
<<<<<<< HEAD
        MultiValueMap<String, Object> body1
                = new LinkedMultiValueMap<>();
=======
        MultiValueMap<String, Object> body1 = new LinkedMultiValueMap<>();
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
        body1.add("profile", profile);
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body1,  headers);
        ResponseEntity<Void> exchange = restTemplate.exchange(uri, HttpMethod.PUT, request1, Void.class);
<<<<<<< HEAD
=======
        assertThat(exchange.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377

        // get updated profile
        ResponseEntity<Profile> profileResponseUpdated = restTemplate
                .withBasicAuth("test3@email.com", "test")
                .getForEntity(uri,  Profile.class);
        assertThat(profileResponseUpdated.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(Objects.requireNonNull(profileResponseUpdated.getBody()).getProfileName()).isEqualTo("Maverick");
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
<<<<<<< HEAD
                    auth.getBytes(Charset.forName("US-ASCII")) );
=======
                    auth.getBytes(StandardCharsets.US_ASCII) );
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }

}
