package fh.server.rest.mapper;

import fh.server.entity.Account;
import fh.server.rest.dto.AccountDTO;
import org.mapstruct.factory.Mappers;

public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    Account convertAccountDTOtoEntity(AccountDTO accountDTO);

    AccountDTO convertEntityToAccountDTO(Account created);
}
