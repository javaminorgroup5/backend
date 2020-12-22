package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.ProfileImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.mapper.ProfileMapper;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static nl.hro.cookbook.service.UserService.getLoginInUser;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/users", "/admin/users"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final CommonService commonService;

    @GetMapping("/login")
    public String login() {
        UserDetails userDetails = getLoginInUser();
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
    public void createUser(@RequestPart("user") User user, @RequestPart("file") MultipartFile file) throws IOException {
        ProfileImage profileImage = new ProfileImage(file.getOriginalFilename(), file.getName(),
                commonService.compressBytes(file.getBytes()));
        user.getProfile().setProfileImage(profileImage);
        userService.createUser(user);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity getProfile(@PathVariable("id") final long id) {
        User user = userService.findUserById(id);
        Profile profile = user.getProfile();
        if (profile != null && profile.getProfileImage() != null) {
            profile.getProfileImage().setPicByte(commonService.decompressBytes(user.getProfile().getProfileImage().getPicByte()));
            return ResponseEntity.ok(profile);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/profile")
    public void updateProfile(@PathVariable("id") final long id, @Valid @RequestBody final Profile profile) {
        userService.updateProfile(id, profile);
    }

}