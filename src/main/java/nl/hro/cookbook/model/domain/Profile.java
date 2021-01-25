package nl.hro.cookbook.model.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.hro.cookbook.security.Role;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Profile {

    @NotNull
    private String profileName;

    @Embedded
    Image image;

    private Role userRole;

    public Profile(@NotNull String profileName, Image image) {
        this.profileName = profileName;
        this.image = image;
    }
}
