package fh.server.rest.mapper;

import fh.server.constant.SpringContext;
import fh.server.entity.Account;
import fh.server.entity.Ingredient;
import fh.server.entity.Order;
import fh.server.entity.Pizza;
import fh.server.repository.IngredientRepository;
import fh.server.repository.PizzaRepository;
import fh.server.rest.dto.AccountDTO;
import fh.server.rest.dto.IngredientDTO;
import fh.server.rest.dto.OrderDTO;
import fh.server.rest.dto.PizzaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
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
    @Mapping(expression = "java(convertIngredientIdList(pizzaDTO.getIngredientIds()))", target = "ingredients")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "available", target = "available")
    Pizza convertPizzaDTOtoEntity(PizzaDTO pizzaDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "desc", target = "desc")
    @Mapping(expression = "java(convertIngredientList(pizza.getIngredients()))", target = "ingredientIds")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "available", target = "available")
    PizzaDTO convertEntityToPizzaDTO(Pizza pizza);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(expression = "java(convertPizzaIdList(orderDTO.getPizzaIds()))", target = "pizzas")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "comment", target = "comment")
    Order convertOrderDTOtoEntity(OrderDTO orderDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(expression = "java(convertPizzaList(order.getPizzas()))", target = "pizzaIds")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "comment", target = "comment")
    OrderDTO convertEntityToOrderDTO(Order order);

    default Set<Ingredient> convertIngredientIdList(Collection<Long> ids) {
        if (ids == null) return null;
        IngredientRepository ingredientRepository = SpringContext.getBean(IngredientRepository.class);
        return ids.stream().map(ingredientRepository::getById).collect(Collectors.toSet());
    }

    default List<Long> convertIngredientList(Collection<Ingredient> ingredients) {
        if (ingredients == null) return null;
        return ingredients.stream().map(Ingredient::getId).collect(Collectors.toList());
    }

    default List<Pizza> convertPizzaIdList(Collection<Long> ids) {
        if (ids == null) return null;
        PizzaRepository pizzaRepository = SpringContext.getBean(PizzaRepository.class);
        return ids.stream().map(pizzaRepository::getById).collect(Collectors.toList());
    }

    default List<Long> convertPizzaList(Collection<Pizza> pizzas) {
        if (pizzas == null) return null;
        return pizzas.stream().map(Pizza::getId).collect(Collectors.toList());
    }
}
