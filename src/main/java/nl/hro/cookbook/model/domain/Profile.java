package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Profile {

    private String profileName;

    @Embedded
    ProfileImage profileImage;
}
