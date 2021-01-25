package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.*;
import nl.hro.cookbook.service.CommonService;
import nl.hro.cookbook.service.UserService;
import nl.hro.cookbook.model.mapper.UserMapper;
import nl.hro.cookbook.model.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nl.hro.cookbook.service.UserService.getLoggedInUser;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/users", "/admin/users"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final CommonService commonService;

    @GetMapping("/login")
    public String login() {
        UserDetails userDetails = getLoggedInUser();
        String username = userDetails.getUsername();
        Optional<User> userFound = userService.findUserByUsername(username);
        return userFound.map(user -> String.valueOf(user.getId())).orElseGet(HttpStatus.NO_CONTENT::getReasonPhrase);
    }

    @GetMapping()
    public Collection<UserDTO> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(@RequestPart("user") User user, @RequestPart("file") MultipartFile file) throws IOException {
        Image profileImage = new Image(file.getOriginalFilename(), file.getName(),
                commonService.compressBytes(file.getBytes()));
        user.getProfile().setImage(profileImage);
        userService.createUser(user);
        return ResponseEntity.ok(user.getId());
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getProfile(@PathVariable("id") final long id) {
        User user = userService.findUserById(id);
        Profile profile = user.getProfile();
        profile.setUserRole(user.getRole());
        if (profile.getImage() != null) {
            profile.getImage().setPicByte(commonService.decompressBytes(user.getProfile().getImage().getPicByte()));
            return ResponseEntity.ok(profile);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}/profile",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateProfile(@PathVariable("id") final long id,
                              @Valid @RequestPart(value = "profile", required = false) final Profile profile,
                              @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        if (file != null) {
            Image profileImage = new Image(file.getOriginalFilename(), file.getName(),
                    commonService.compressBytes(file.getBytes()));
            profile.setImage(profileImage);
        }
        userService.updateProfile(id, profile);
    }

    @GetMapping("/{id}/enrolled")
    public ResponseEntity<?> getEnrolledGroupsForUser(@PathVariable("id") final long id) {
        List<String> enrolledGroupsForUser = userService.findEnrolledGroupsForUser(id);
        if (enrolledGroupsForUser.isEmpty()) {
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
        return ResponseEntity.ok(enrolledGroupsForUser);
    }

    /**
     * Get all messages for the feed with the gives userId;
     *
     * @param userId
     * @return
     */
    @GetMapping("/{user_id}/feed")
    public ResponseEntity<?> getFeedForUser(@PathVariable("user_id") final long userId) {
        List<Message> feedByUserId = userService.findFeedByUserId(userId);
        if (feedByUserId.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        feedByUserId.forEach(message -> {
            if (message.getImage() != null) {
                message.getImage().setPicByte(commonService.decompressBytes(message.getImage().getPicByte()));
            }
        });
        return ResponseEntity.ok(feedByUserId);
    }
}
