package nl.hro.cookbook.model.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShareLink extends BaseEntity {
    private String shareLink;
}
