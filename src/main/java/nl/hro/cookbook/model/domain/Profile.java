package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private String profileName;

    @Embedded
    ProfileImage profileImage;

    @Override
    public String toString() {
        return "Profile{" +
                "profileName='" + profileName + '\'' +
                ", profileImage=" + profileImage +
                '}';
    }
}
