package nl.hro.cookbook.model.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Category extends BaseEntity {

    @NotNull
    private String categoryName;

    private Boolean active = true;

    @OneToMany
    @JoinTable
    private List<Group> groups;
}
