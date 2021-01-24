package nl.hro.cookbook.model.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@Getter
@Setter
@ToString
public class Category extends BaseEntity {

    private String categoryName;

    @OneToMany
    @JoinTable
    private List<Group> groups;
}
