package ch.bookoflies.qsserver.rest.mapper;

import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.rest.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "token", target = "token")
    User convertUserDTOtoEntity(UserDTO userDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "token", target = "token")
    UserDTO convertEntityToUserDTO(User created);
}
