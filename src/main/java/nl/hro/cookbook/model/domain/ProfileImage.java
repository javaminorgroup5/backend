package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Arrays;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProfileImage {

    private String name;

    private String type;

    @Lob
    @Column(name = "picByte", length = 1000000)
    private byte[] picByte;
}
