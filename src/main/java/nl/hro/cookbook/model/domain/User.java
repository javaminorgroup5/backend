package nl.hro.cookbook.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.hro.cookbook.security.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(exclude = "role")
@Getter
@Setter
@ToString
public class User extends BaseEntity {

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Profile profile;

    @JsonIgnore
    @ManyToMany(mappedBy = "enrolledUsers")
    List<Group> enrolledGroups;
}
