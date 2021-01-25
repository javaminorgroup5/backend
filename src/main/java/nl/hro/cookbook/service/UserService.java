package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.*;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.RecipeRepository;
import nl.hro.cookbook.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecipeRepository recipeRepository;
    private final TestDataService testDataService;
    private final MessageService messageService;

    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No user exists for id: %d", userId), User.class));
    }

    public List<Profile> getAllUserProfiles() {
        List<User> users = userRepository.findAll();
        return users.stream().map(User::getProfile).collect(Collectors.toList());
    }

    @Transactional
    public void createUser(User user) throws IOException {
        boolean valid = EmailValidator.getInstance().isValid(user.getEmail());
        if (valid) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            throw new IOException("Invalid email address");
        }
    }

    @Transactional
    public List<Message> findFeedByUserId(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return Collections.emptyList();
        }
        Optional<List<Message>> messagesByUserId = messageService.findMessagesByUserId(id);
        if (messagesByUserId.isEmpty()) {
            return Collections.emptyList();
        }
        return messagesByUserId.get();
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByEmail(username);
    }

    @Transactional
    public void updateProfile(final long userId, final Profile newProfile) {
        User user = findUserById(userId);
        if (user == null) {
            return;
        }
        Image profileImage = newProfile.getImage();
        if (profileImage != null) {
            user.getProfile().setImage(newProfile.getImage());
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


    public List<String> findEnrolledGroupsForUser(long id) {
        List<String> groupNames = new ArrayList<>();
        User user = findUserById(id);
        for (Group group : user.getEnrolledGroups()) {
            String groupName = group.getGroupName();
            groupNames.add(groupName);
        }
        return groupNames;
    }


}
