package fh.server.service;

import fh.server.entity.Ingredient;
import fh.server.entity.Pizza;
import fh.server.repository.IngredientRepository;
import fh.server.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    public PizzaService(
            @Qualifier("pizzaRepository") PizzaRepository pizzaRepository
    ) {
        this.pizzaRepository = pizzaRepository;
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

    private void checkNameUniqueness(String name) {

    }
}
