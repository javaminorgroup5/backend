package nl.hro.cookbook.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.hro.cookbook.model.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UserDetailsAdapter implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Should ideally be implemented
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Should ideally be implemented
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Should ideally be implemented
    }

    @Override
    public boolean isEnabled() {
        return true; // Should ideally be implemented
    }
}