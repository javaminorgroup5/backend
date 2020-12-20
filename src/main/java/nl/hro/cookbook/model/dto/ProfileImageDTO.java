package nl.hro.cookbook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageDTO {

    private String name;

    private String type;

    @Column(name = "picByte", length = 100000000)
    private byte[] picByte;

}
