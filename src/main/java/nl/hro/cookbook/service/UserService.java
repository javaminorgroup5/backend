package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.domain.Address;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.UserRepository;
import nl.hro.cookbook.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No user exists for id: %d", userId), User.class));
    }

    @Transactional(readOnly = false)
    public void updateAddress(final long userId, final Address newAddress) {
        final User user = findUserById(userId);
        user.setAddress(newAddress);
        userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public void addFriends(final long userId, final Collection<Long> newFriends) {
        final Collection<User> friends = newFriends.stream()
                .map(this::findUserById)
                .collect(Collectors.toList());

        final User user = findUserById(userId);
        user.getFriends().addAll(friends);

        userRepository.save(user);
    }


//    This is a pretty hacky way to have a user available on startup.
//    This is fine for a demo, but don't do this in real code.
    @PostConstruct
    public void init() {
        final User initialUser1 = new User(0, "dion", passwordEncoder.encode("quintor"), Role.ADMIN, new Address("Den Bosch", "Havensingel", 1), Collections.emptyList()); // no friends :(
        final User initialUser2 = new User(0, "geoffrey", passwordEncoder.encode("quintor"), Role.COMMUNITY_MANAGER, new Address("Den Haag", "Lange Vijverberg", 4), Collections.singletonList(initialUser1)); // yay a friend :)

        userRepository.saveAll(Arrays.asList(initialUser1, initialUser2));

    }
}