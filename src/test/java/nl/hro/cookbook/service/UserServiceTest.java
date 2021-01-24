package nl.hro.cookbook.service;

import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userServiceTest;

    @Test
    void createUserTest() throws IOException {
        // Given
        User user = new User();
        user.setEmail("john@test.nl");
        user.setPassword(passwordEncoder.encode("password"));
        lenient().when(userRepository.findUserByEmail(eq("john@test.nl"))).thenReturn(Optional.of(user));

        // When
        userServiceTest.createUser(user);

        // Then
        Optional<User> userOptional = userRepository.findUserByEmail("john@test.nl");
        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());

    }

    @Test
    void updateUserProfileTest() throws IOException {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.nl");
        user.setPassword(passwordEncoder.encode("password"));
        Profile profile = new Profile();
        profile.setProfileName("Tom");
        user.setProfile(profile);
        lenient().when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        userServiceTest.createUser(user);

        // When
        userServiceTest.updateProfile(1, profile);

        // Then
        Optional<User> userOptional = userRepository.findById(1L);
        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get().getProfile().getProfileName().equals("Jerry"));

    }

}
