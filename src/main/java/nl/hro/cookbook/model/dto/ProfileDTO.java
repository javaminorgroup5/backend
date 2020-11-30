package nl.hro.cookbook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    @NotNull
    private String profileName;

    @NotNull
    private String profilePicture;

}
