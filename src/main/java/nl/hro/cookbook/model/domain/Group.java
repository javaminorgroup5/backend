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

    private String groupName;

    private String description;

    private Long userId;

    @Column
    @ElementCollection(targetClass=Profile.class)
    private List<Profile> profiles;

    @OneToMany
    @JoinTable
    private List<Invite> invites;

    @OneToMany
    @JoinTable
    private List<Message> feed;

    @ManyToMany
    @JoinTable
    private List<User> enrolledUsers;

    @Embedded
    private GroupImage groupImage;

}
