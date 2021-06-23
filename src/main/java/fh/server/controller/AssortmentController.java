package fh.server.controller;

import fh.server.rest.dto.IngredientDTO;
import fh.server.rest.dto.PizzaDTO;
import fh.server.service.AssortmentService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class AssortmentController {

    private final AssortmentService supplyService;

    public AssortmentController(AssortmentService supplyService) {
        this.supplyService = supplyService;
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

    public void removeIngredient(String name) {
        // TODO
    }

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

    public void removePizza(String name) {
        // TODO
    }


}
