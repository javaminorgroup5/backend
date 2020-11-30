package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.dto.AddressDTO;
import nl.hro.cookbook.model.dto.FriendDTO;
import nl.hro.cookbook.service.UserService;
import nl.hro.cookbook.model.mapper.UserMapper;
import nl.hro.cookbook.model.mapper.AddressMapper;
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
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @CrossOrigin(origins = "*")
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

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") final long id) {
        return userMapper.toDTO(userService.findUserById(id));
    }

    @GetMapping("/{id}/address")
    public AddressDTO getUserAddress(@PathVariable("id") final long id) {
        return addressMapper.toDTO(userService.findUserById(id).getAddress());
    }

    @PutMapping("/{id}/address")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAddress(@PathVariable("id") final long id, @Valid @RequestBody final AddressDTO addressDTO) {
        userService.updateAddress(id, addressMapper.toModel(addressDTO));
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDTO> getUserFriends(@PathVariable("id") final long id) {
        return userService.findUserById(id)
                .getFriends().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFriends(@PathVariable("id") final long id, @Valid @RequestBody Collection<FriendDTO> friendDTOS) {
        userService.addFriends(id, friendDTOS.stream().map(FriendDTO::getUserId).collect(Collectors.toList()));
    }
}