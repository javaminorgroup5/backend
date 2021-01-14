package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Arrays;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GroupImage {

    private String name;

    private String type;

    @Column(name = "picByte", length = 100000000)
    private byte[] picByte;
}
