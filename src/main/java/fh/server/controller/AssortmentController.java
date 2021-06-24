package fh.server.controller;

import fh.server.rest.dto.IngredientDTO;
import fh.server.rest.dto.PizzaDTO;
import fh.server.service.PizzaService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class AssortmentController {

    private final PizzaService assortmentService;
    private final AccountController accountController;

    public AssortmentController(PizzaService assortmentService, AccountController accountController) {
        this.assortmentService = assortmentService;
        this.accountController = accountController;
    }

    public Collection<IngredientDTO> getIngredients() {
        // TODO
        return null;
    }

    public IngredientDTO addIngredient(IngredientDTO ingredientDTO) {
        // TODO
        return null;
    }

    public IngredientDTO updateIngredient(IngredientDTO ingredientDTO) {
        // TODO
        return null;
    }

//    public void deleteIngredient(String name) {
//
//    }

    public Collection<PizzaDTO> getPizzas() {
        // TODO
        return null;
    }

    public PizzaDTO addPizza(PizzaDTO pizzaDTO) {
        // TODO
        return null;
    }

    public PizzaDTO updatePizza(PizzaDTO pizzaDTO) {
        // TODO
        return null;
    }

//    public void deletePizza(String name) {
//
//    }


}
