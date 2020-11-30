package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Address;
import nl.hro.cookbook.model.dto.AddressDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper extends ModelToDTOMapper<Address, AddressDTO> {
}
