package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_recipes")
public class Recipe extends BaseEntity {

    @NotNull
    private String title;

    @NotNull
    @Size(min = 1, max = 10000)
    private String recipe;

    @NotNull
    @Size(min = 1, max = 10000)
    private String description;

    @NotNull
    private Long userId;

    @NotNull
    private Long groupId;

    @Embedded
    Image image;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany
    @JoinTable
    List<ShareLink> shareLinks;
}
