package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.ProfileImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.UserRepository;
import nl.hro.cookbook.security.Role;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonService commonService;

    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No user exists for id: %d", userId), User.class));
    }

    @Transactional()
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Transactional()
    public void updateProfile(final long userId, final Profile newProfile) {
        User user = findUserById(userId);
        if (user == null) {
            return;
        }
        ProfileImage profileImage = newProfile.getProfileImage();
        if (profileImage != null) {
            byte[] picByte = profileImage.getPicByte();
            user.getProfile().getProfileImage().setPicByte(picByte);
        }
        String profileName = newProfile.getProfileName();
        if (profileName != null && user.getProfile() != null) {
            user.getProfile().setProfileName(profileName);
        }
        user.setProfile(user.getProfile());
        userRepository.save(user);
    }

//    This is a pretty hacky way to have a user available on startup.
//    This is fine for a demo, but don't do this in real code.
    @PostConstruct
    public void init() throws Exception {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:download.jpeg");
        ProfileImage profileImage = new ProfileImage("test.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource.getFile().toPath())));
        final User initialUser1 = new User(1L, "dion", passwordEncoder.encode("quintor"), Role.ADMIN, new Profile("Top", profileImage));
        final User initialUser2 = new User(2L, "geoffrey", passwordEncoder.encode("quintor"), Role.COMMUNITY_MANAGER, new Profile("Maverick", profileImage));
        userRepository.saveAll(Arrays.asList(initialUser1, initialUser2));
    }

    public static UserDetails getLoginInUser() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}