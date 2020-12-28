package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileImageDTO {

    private String name;
    private String type;
    private byte[] picByte;

}
