package nl.hro.cookbook.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Guard {

    public boolean checkWriteAccess(final Authentication authentication, final int userId) {
        final User user = ((UserDetailsAdapter) authentication.getPrincipal()).getUser();

//        final boolean allowed = isUser(user, recipeId) || (isCommunityManager(user, recipeId) && isFriend(user, recipeId));
        boolean allowed = true; // TODO temp


        if(allowed) {
            log.info("User {} tried to write to user {} [ALLOWED]", user.getId(), userId);
        } else {
            log.warn("User {} tried to write to user {} [DENIED]", user.getId(), userId);
        }

        return allowed;
    }

    public boolean checkReadAccess(final Authentication authentication, final int userId) {
        final User user = ((UserDetailsAdapter) authentication.getPrincipal()).getUser();

//        final boolean allowed =  isFriend(user, recipeId) || isUser(user, recipeId);

        boolean allowed = true; // TODO temp

        if(allowed) {
            log.info("User {} tried to read from user {} [ALLOWED]", user.getId(), userId);
        } else {
            log.warn("User {} tried to read from user {} [DENIED]", user.getId(), userId);
        }

        return allowed;
    }

    private boolean isCommunityManager(final User user, final String username) {
        return user.getEmail().equals(username) && user.getRole().equals(Role.COMMUNITY_MANAGER);
    }

    private boolean isUser(final User user, final int userId) {
        return user.getId() == userId;
    }

}

