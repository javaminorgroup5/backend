package nl.hro.cookbook.model.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Invite extends BaseEntity {

    private String token;
}
