package nl.hro.cookbook.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
@Getter
@Setter
@ToString
public class Group extends BaseEntity {

    private String groupName;

    private String description;

    private Long userId;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn
    private Category category;

    public enum GroupPrivacy {
        PRIVATE,
        INVITE,
        OPEN
    }
    private GroupPrivacy groupPrivacy;

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
    Image image;
}
