package nl.hro.cookbook.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String username;
    private AddressDTO address;
}