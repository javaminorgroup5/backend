package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Long userId;
    @Column
    @ElementCollection(targetClass=Profile.class)
    private List<Profile> profiles;


    @OneToMany
    @JoinTable
    List<Invite> invites;

    @ManyToMany
    @JoinTable
    List<User> enrolledUsers;

    @Embedded
    private GroupImage groupImage;
}
