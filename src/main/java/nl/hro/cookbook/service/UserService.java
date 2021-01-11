package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.ProfileImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.RecipeRepository;
import nl.hro.cookbook.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecipeRepository recipeRepository;
    private final TestDataService testDataService;

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
            user.getProfile().setProfileImage(newProfile.getProfileImage());
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
        userRepository.saveAll(testDataService.getUsers());
        recipeRepository.saveAll(testDataService.getRecipes());
    }

    public static UserDetails getLoggedInUser() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public List<String> getEnrolledGroupsForUser(long userId) {
        List<String> groupIds = new ArrayList<>();
        User user = findUserById(userId);

        for (Iterator<Group> i = user.getEnrolledGroups().iterator(); i.hasNext(); ) {
            Group item = i.next();
            String groupId = item.getName();
            groupIds.add(groupId);
        }
        return groupIds;
    }
}
