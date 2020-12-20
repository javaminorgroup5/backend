package nl.hro.cookbook.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Arrays;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImage {

    private String name;

    private String type;

    @Column(name = "picByte", length = 100000000)
    private byte[] picByte;

    @Override
    public String toString() {
        return "ProfileImage{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", picByte=" + Arrays.toString(picByte) +
                '}';
    }
}
