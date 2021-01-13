package nl.hro.cookbook.controller;

import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.GroupImage;
import nl.hro.cookbook.model.domain.Message;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static nl.hro.cookbook.controller.ImageHelper.createTempFileResource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GroupControllerTest {

    @LocalServerPort
    private int port;
    private User user;
    private List<Group> groups;
    private List<Message> messages;

    @BeforeEach
    void setUp() {
        final Message message1 = new Message(1L, "This is my first message", 12L);
        final Message message2 = new Message(2L, "This is my second message", 12L);
        final Message message3 = new Message(3L, "This is my third message", 12L);
        GroupImage groupImage = new GroupImage("group.jpg", "file", new byte[12]);
        final Group initialGroup1 = new Group(1L, "PastaGroep", "Leuke pasta groep", 12L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup2 = new Group(2L, "RodeSauzen", "Roder dan rood", 12L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup3 = new Group(3L, "Bloemkoollovers", "Bloemkool is een groente die hoort bij het geslacht kool uit de kruisbloemenfamilie (Brassicaceae). De botanische naam voor bloemkool is Brassica oleracea convar. ", 12L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), groupImage);
        groups = Arrays.asList(initialGroup1, initialGroup2, initialGroup3);
        messages = Arrays.asList(message1, message2, message3);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createGroupResponse() throws Exception {
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

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test1@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        body.add("group", groups.get(0));
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/group/create/" + 4);
        ResponseEntity response1 = restTemplate
                .withBasicAuth("test1@email.com", "test")
                .postForEntity(uri1, request1, Void.class);
        assertThat(response1.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void postMessageToGroupFeedResponse() throws Exception {
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

        uri = new URI("http://localhost:" + port + "/users/login");
        ResponseEntity<String> stringResponse = restTemplate
                .withBasicAuth("test1@email.com", "test")
                .getForEntity(uri,  String.class);
        assertThat(stringResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("file", createTempFileResource("test.jpg".getBytes()));
        body.add("group", groups.get(0));
        HttpEntity<MultiValueMap<String, Object>> request1 =
                new HttpEntity<>(body,  headers);
        URI uri1 = new URI("http://localhost:" + port + "/group/create/" + 4);
        ResponseEntity response1 = restTemplate
                .withBasicAuth("test1@email.com", "test")
                .postForEntity(uri1, request1, Void.class);
        assertThat(response1.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Message> request2 =
                new HttpEntity<>(messages.get(0),  headers);
        URI uri2 = new URI("http://localhost:" + port + "/group/"+ groups.get(0).getId() + "/feed");
        ResponseEntity response2 = restTemplate
                .withBasicAuth("test1@email.com", "test")
                .postForEntity(uri2, request2, Void.class);
        assertThat(response2.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }
}
