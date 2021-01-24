package nl.hro.cookbook.model.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shareLink")
@Getter
@Setter
public class ShareLink extends BaseEntity {
    private String shareLink;
}
