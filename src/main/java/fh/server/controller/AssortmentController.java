package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.Ingredient;
import fh.server.entity.Pizza;
import fh.server.rest.dto.IngredientDTO;
import fh.server.rest.dto.PizzaDTO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.service.AccountService;
import fh.server.service.IngredientService;
import fh.server.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AssortmentController {

    private final IngredientService ingredientService;
    private final PizzaService pizzaService;
    private final AccountService accountService;

    public AssortmentController(
            IngredientService ingredientService,
            PizzaService pizzaService,
            AccountService accountService
    ) {
        this.ingredientService = ingredientService;
        this.pizzaService = pizzaService;
        this.accountService = accountService;
    }

    /**
     * fetches all ingredients from the database.
     * can be filtered by :
     * - availability
     * @return list of found ingredients
     */
    @GetMapping("ingredients/all")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<IngredientDTO> getIngredients(
            @RequestParam("available") Optional<Boolean> available
    ) {
        List<IngredientDTO> list = new ArrayList<>();
        for (Ingredient ingredient : ingredientService.getIngredients()) {
            list.add(DTOMapper.INSTANCE.convertEntityToIngredientDTO(ingredient));
        }
        if (available.isPresent()) list = list.stream().filter(e -> available.get().equals(e.getAvailable())).collect(Collectors.toList());
        return list;
    }

    /**
     * creates an ingredient
     * @param ingredientDTO blueprint
     * @param accountId client id (must have chef clearance level)
     * @param token verification token
     * @return created ingredient
     */
    @PostMapping("ingredients/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public IngredientDTO createIngredient(
            @RequestBody IngredientDTO ingredientDTO,
            @RequestHeader("accountId") Long accountId,
            @RequestHeader("token") String token
    ) {
        Account account = accountService.authenticateAccount(accountId, token, Account.CLEARANCE_LEVEL_CHEF);
        Ingredient blueprint = DTOMapper.INSTANCE.convertIngredientDTOtoEntity(ingredientDTO);
        Ingredient created = ingredientService.createIngredient(blueprint, account);
        return DTOMapper.INSTANCE.convertEntityToIngredientDTO(created);
    }

    /**
     * updates an ingredient
     * @param ingredientDTO blueprint
     * @param accountId client id (must have chef clearance level)
     * @param token verification token
     * @param ingredientId ingredient id
     * @return updated ingredient
     */
    @PutMapping("ingredients/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public IngredientDTO updateIngredient(
            @RequestBody IngredientDTO ingredientDTO,
            @RequestHeader("accountId") Long accountId,
            @RequestHeader("token") String token,
            @RequestHeader("ingredientId") Long ingredientId
    ) {
        Account account = accountService.authenticateAccount(accountId, token, Account.CLEARANCE_LEVEL_CHEF);
        Ingredient blueprint = DTOMapper.INSTANCE.convertIngredientDTOtoEntity(ingredientDTO);
        Ingredient updated = ingredientService.updateIngredient(ingredientId, blueprint, account);
        return DTOMapper.INSTANCE.convertEntityToIngredientDTO(updated);
    }

//    public void deleteIngredient(String name) {
//
//    }

    /**
     * fetches all pizzas from the database.
     * can be filtered by :
     * - availability
     * @return list of found pizzas
     */
    @GetMapping("pizzas/all")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<PizzaDTO> getPizzas(
            @RequestParam("available") Optional<Boolean> available
    ) {
        List<PizzaDTO> list = new ArrayList<>();
        for (Pizza pizza : pizzaService.getPizzas()) {
            list.add(DTOMapper.INSTANCE.convertEntityToPizzaDTO(pizza));
        }
        if (available.isPresent()) list = list.stream().filter(e -> available.get().equals(e.getAvailable())).collect(Collectors.toList());
        return list;
    }

    /**
     * creates a pizza
     * @param pizzaDTO blueprint
     * @param accountId client id (must have chef clearance level)
     * @param token verification token
     * @return created pizza
     */
    @PostMapping("pizzas/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PizzaDTO addPizza(
            @RequestBody PizzaDTO pizzaDTO,
            @RequestHeader("accountId") Long accountId,
            @RequestHeader("token") String token
    ) {
        Account account = accountService.authenticateAccount(accountId, token, Account.CLEARANCE_LEVEL_CHEF);
        Pizza blueprint = DTOMapper.INSTANCE.convertPizzaDTOtoEntity(pizzaDTO);
        Pizza created = pizzaService.createPizza(blueprint, account);
        return DTOMapper.INSTANCE.convertEntityToPizzaDTO(created);
    }

    /**
     * updates a pizza
     * @param pizzaDTO blueprint
     * @param accountId client id (must have chef clearance level)
     * @param token verification token
     * @param pizzaId pizza id
     * @return updated pizza
     */
    @PutMapping("pizzas/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PizzaDTO updatePizza(
            @RequestBody PizzaDTO pizzaDTO,
            @RequestHeader("accountId") Long accountId,
            @RequestHeader("token") String token,
            @RequestHeader("pizzaId") Long pizzaId
    ) {
        Account account = accountService.authenticateAccount(accountId, token, Account.CLEARANCE_LEVEL_CHEF);
        Pizza blueprint = DTOMapper.INSTANCE.convertPizzaDTOtoEntity(pizzaDTO);
        Pizza updated = pizzaService.updatePizza(pizzaId, blueprint, account);
        return DTOMapper.INSTANCE.convertEntityToPizzaDTO(updated);
    }

//    public void deletePizza(String name) {
//
//    }


}
