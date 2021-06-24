package fh.server.rest.mapper;

import fh.server.entity.Account;
import fh.server.entity.Ingredient;
import fh.server.entity.Order;
import fh.server.entity.Pizza;
import fh.server.rest.dto.AccountDTO;
import fh.server.rest.dto.IngredientDTO;
import fh.server.rest.dto.OrderDTO;
import fh.server.rest.dto.PizzaDTO;
import org.mapstruct.factory.Mappers;

public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    Account convertAccountDTOtoEntity(AccountDTO accountDTO);

    AccountDTO convertEntityToAccountDTO(Account created);

    Ingredient convertIngredientDTOtoEntity(IngredientDTO ingredientDTO);

    IngredientDTO convertEntityToIngredientDTO(Ingredient ingredient);

    Pizza convertPizzaDTOtoEntity(PizzaDTO pizzaDTO);

    PizzaDTO convertEntityToPizzaDTO(Pizza pizza);

    Order convertOrderDTOtoEntity(OrderDTO orderDTO);

    OrderDTO convertEntityToOrderDTO(Order order);
}
