package fh.server.rest.mapper;

import fh.server.constant.SpringContext;
import fh.server.entity.Account;
import fh.server.entity.Ingredient;
import fh.server.entity.Order;
import fh.server.entity.Pizza;
import fh.server.repository.IngredientRepository;
import fh.server.rest.dto.AccountDTO;
import fh.server.rest.dto.IngredientDTO;
import fh.server.rest.dto.OrderDTO;
import fh.server.rest.dto.PizzaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "clearanceLevel", target = "clearanceLevel")
    Account convertAccountDTOtoEntity(AccountDTO accountDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "clearanceLevel", target = "clearanceLevel")
    AccountDTO convertEntityToAccountDTO(Account created);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "desc", target = "desc")
    @Mapping(source = "available", target = "available")
    Ingredient convertIngredientDTOtoEntity(IngredientDTO ingredientDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "desc", target = "desc")
    @Mapping(source = "available", target = "available")
    IngredientDTO convertEntityToIngredientDTO(Ingredient ingredient);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "desc", target = "desc")
    @Mapping(expression = "java(convertIngredientIdList(pizzaDTO.getIngredients()))", target = "ingredients")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "available", target = "available")
    Pizza convertPizzaDTOtoEntity(PizzaDTO pizzaDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "desc", target = "desc")
    @Mapping(source = "ingredients", target = "ingredients")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "available", target = "available")
    PizzaDTO convertEntityToPizzaDTO(Pizza pizza);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "pizzaIds", target = "pizzaIds")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "comment", target = "comment")
    Order convertOrderDTOtoEntity(OrderDTO orderDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "pizzaIds", target = "pizzaIds")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "comment", target = "comment")
    OrderDTO convertEntityToOrderDTO(Order order);

    default Set<Ingredient> convertIngredientIdList(Set<Long> ids) {
        IngredientRepository ingredientRepository = SpringContext.getBean(IngredientRepository.class);
        return ids.stream().map(ingredientRepository::getById).collect(Collectors.toSet());
    }
}
