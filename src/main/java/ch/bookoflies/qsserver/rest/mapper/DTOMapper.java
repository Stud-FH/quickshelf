package ch.bookoflies.qsserver.rest.mapper;

import ch.bookoflies.qsserver.persistent.Library;
import ch.bookoflies.qsserver.persistent.LocalLogin;
import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.rest.dto.LibraryDTO;
import ch.bookoflies.qsserver.rest.dto.LoginDTO;
import ch.bookoflies.qsserver.rest.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


    @Mapping(source = "name", target = "name")
    @Mapping(source = "token", target = "token")
    User convertUserDTOtoEntity(UserDTO userDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "token", target = "token")
    UserDTO convertEntityToUserDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    LocalLogin convertLoginDTOtoEntity(LoginDTO loginDTO);

    Library convertLibraryDTOtoEntity(LibraryDTO libraryDTO);
    LibraryDTO convertEntityToLibraryDTO(Library library);
}
