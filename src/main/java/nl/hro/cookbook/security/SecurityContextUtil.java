package nl.hro.cookbook.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {

    private SecurityContextUtil() {
        // Don't instantiate me
    }

    public static UserDetailsAdapter getSecurityContextUser() {
        return (UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}