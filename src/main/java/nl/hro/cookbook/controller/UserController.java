package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.dto.ProfileDTO;
import nl.hro.cookbook.model.mapper.ProfileMapper;
import nl.hro.cookbook.service.UserService;
import nl.hro.cookbook.model.mapper.UserMapper;
import nl.hro.cookbook.model.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/users", "/admin/users"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;


    @GetMapping("/login")
    public HttpStatus login() {
        return HttpStatus.OK;
    }

    @GetMapping()
    public Collection<UserDTO> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/profile")
    public ProfileDTO getUserProfile(@PathVariable("id") final long id) {
        return profileMapper.toDTO(userService.findUserById(id).getProfile());
    }

    @PutMapping("/{id}/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@PathVariable("id") final long id, @Valid @RequestBody final ProfileDTO profileDTO) {
        userService.updateProfile(id, profileMapper.toModel(profileDTO));
    }

}