package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
@Getter
@Setter
@ToString
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
    private List<Invite> invites;

    @OneToMany
    @JoinTable
    private List<Message> messages;


    @ManyToMany
    @JoinTable
    private List<User> enrolledUsers;

    @Embedded
    private Image image;


}
