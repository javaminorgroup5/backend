package nl.hro.cookbook.controller;

import nl.hro.cookbook.model.domain.Like;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.security.Role;
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

import static nl.hro.cookbook.controller.ImageHelper.createTempFileResource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LikeControllerTest {

    @LocalServerPort
    private int port;
    private final long userId = 12L;
    private final long messageId = 2L;
    private final long recipeId = 3L;
    private final Like like = new Like(userId, messageId, recipeId);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void saveLikeResponse() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        User user = new User( "test33@email.com", "test", Role.COMMUNITY_MANAGER,
                new Profile("Top Gun", null), new ArrayList<>());
        body.add("user", user);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body,  headers);
        URI uri = new URI("http://localhost:" + port + "/users/create");
        ResponseEntity<?> response = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test33@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Like> request1 =
                new HttpEntity<>(like,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/likes/message");
        ResponseEntity<?> response1 = restTemplate
                .withBasicAuth("test33@email.com", "test")
                .postForEntity(uri1, request1, String.class);
        assertThat(response1.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response1.getBody()).isNotNull();

        String authStr = "test33@email.com:test";
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request5 = new HttpEntity<>(headers);
        uri = new URI("http://localhost:" + port + "/likes/message/" + messageId);
        ResponseEntity<List<Like>> likesResponse = restTemplate
                .exchange(uri, HttpMethod.GET, request5,  new ParameterizedTypeReference<List<Like>>() {});
        assertThat(likesResponse.getBody()).isNotEmpty();
        assertThat(Objects.requireNonNull(likesResponse.getBody()).size()).isEqualTo(1);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Like> request2 =
                new HttpEntity<>(like,  headers);
        URI uri2 = new URI("http://localhost:" + port + "/likes/message");
        ResponseEntity<?> response2 = restTemplate
                .withBasicAuth("test33@email.com", "test")
                .postForEntity(uri2, request2, String.class);
        assertThat(response2.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.getBody()).isNull();
    }
}
