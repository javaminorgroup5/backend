package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
@Getter
@Setter
@ToString
public class Like extends BaseEntity {
    @NotNull
    @UniqueElements
    private long userId;

    private long messageId;

    private long recipeId;
}
