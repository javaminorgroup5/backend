package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.hro.cookbook.security.Role;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(exclude = "role")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Profile profile;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", profile=" + profile +
                '}';
    }

    @ManyToMany(mappedBy = "enrolledUsers")
    List<Group> enrolledGroups;
}
