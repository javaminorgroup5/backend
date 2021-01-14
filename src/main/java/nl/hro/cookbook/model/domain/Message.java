package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
@Getter
@Setter
@ToString
public class Message extends BaseEntity {
    @NotNull
    private String message;

    @NotNull
    private Long userId;

    @NotNull
    private Long groupId;
}