package fh.server.service;

import fh.server.entity.Ingredient;
import fh.server.entity.Pizza;
import fh.server.repository.IngredientRepository;
import fh.server.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AssortmentService {

    private final IngredientRepository ingredientRepository;
    private final PizzaRepository pizzaRepository;

    public AssortmentService(
            @Qualifier("ingredientRepository") IngredientRepository ingredientRepository,
            @Qualifier("pizzaRepository") PizzaRepository pizzaRepository
    ) {
        this.ingredientRepository = ingredientRepository;
        this.pizzaRepository = pizzaRepository;
    }



    public Collection<Ingredient> getIngredients() {
        // TODO
        return null;
    }

    public Ingredient addIngredient(Ingredient blueprint) {
        // TODO
        return null;
    }

    public Ingredient updateIngredient(Ingredient blueprint) {
        // TODO
        return null;
    }

    public void removeIngredient(String name) {
        // TODO
    }

    public Collection<Pizza> getPizzas() {
        // TODO
        return null;
    }

    public Pizza addPizza(Pizza blueprint) {
        // TODO
        return null;
    }

    public Pizza updatePizza(Pizza blueprint) {
        // TODO
        return null;
    }

    public void removePizza(String name) {
        // TODO
    }
}
