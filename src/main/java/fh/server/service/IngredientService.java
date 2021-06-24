package fh.server.service;

import fh.server.entity.Account;
import fh.server.entity.Ingredient;
import fh.server.entity.Pizza;
import fh.server.repository.IngredientRepository;
import fh.server.repository.PizzaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class IngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;
    private final PizzaRepository pizzaRepository;

    public IngredientService(
            @Qualifier("ingredientRepository") IngredientRepository ingredientRepository,
            @Qualifier("pizzaRepository") PizzaRepository pizzaRepository
    ) {
        this.ingredientRepository = ingredientRepository;
        this.pizzaRepository = pizzaRepository;
    }

    /**
     * fetches all ingredients from the repository
     * @return list of all known ingredients
     */
    public Collection<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    /**
     * creates a new ingredient entity
     * @param blueprint ingredient blueprint
     * @param account caller
     * @return created ingredient
     */
    public Ingredient createIngredient(Ingredient blueprint, Account account) {
        checkNameFormat(blueprint.getName());
        checkNameUniqueness(blueprint.getName());
        checkDescFormat(blueprint.getDesc());
        if (blueprint.getAvailable() == null) blueprint.setAvailable(true);
        Ingredient created = ingredientRepository.saveAndFlush(blueprint);
        LOGGER.info(account.getEmail() + " created ingredient " + created.getName());
        return created;
    }

    /**
     * updates an ingredient.
     * @param id ingredient to update
     * @param blueprint attributes to update
     * @param account caller
     * @return updated ingredient
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this update;
     *          404 if no ingredient is found;
     *          In case of an exception, none of the updates are performed.
     */
    public Ingredient updateIngredient(Long id, Ingredient blueprint, Account account) {
        if (!ingredientRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid ingredient id");

        if (blueprint.getName() != null) {
            checkNameFormat(blueprint.getName());
            checkNameUniqueness(blueprint.getName());
        }
        if (blueprint.getDesc() != null) {
            checkDescFormat(blueprint.getDesc());
        }

        Ingredient ingredient = ingredientRepository.getById(id);
        if (blueprint.getName() != null) {
            ingredient.setName(blueprint.getName());
        }
        if (blueprint.getDesc() != null) {
            ingredient.setDesc(blueprint.getDesc());
        }
        if (blueprint.getAvailable() != null) {
            ingredient.setAvailable(blueprint.getAvailable());
        }
        ingredientRepository.flush();
        LOGGER.info(account.getEmail() + " updated ingredient " + ingredient.getName());
        return ingredient;
    }

    /**
     * deletes an ingredient from the repository.
     * before deletion, the ingredient is removed from all the pizzas using it (list is printed to console).
     * @param id ingredient to delete
     * @param account caller
     * @throws ResponseStatusException 404 if no ingredient is found
     */
    public void deleteIngredient(Long id, Account account) {
        if (!ingredientRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid ingredient id");

        Ingredient ingredient = ingredientRepository.getById(id);
        LOGGER.info(account.getEmail() + " deleted ingredient " + ingredient.getName());

        Collection<Pizza> affectedPizzas = pizzaRepository.findAllByIngredientsContaining(ingredient);
        for (Pizza pizza : affectedPizzas) {
            pizza.removeIngredient(ingredient);
            LOGGER.info("removed ingredient from pizza " + pizza.getName());
        }
        pizzaRepository.flush();
        ingredientRepository.deleteById(id);
        ingredientRepository.flush();
    }

    /**
     * checks if a String is a valid ingredient name
     * @param name the String to check
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this name
     */
    private void checkNameFormat(String name) {
        if (name == null || name.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name must be defined");
    }

    /**
     * checks if a String is a valid ingredient description
     * @param desc the String top check
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this description
     */
    private void checkDescFormat(String desc) {
        if (desc.length() >= 255)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "description too long");
    }

    /**
     * checks whether an ingredient name is unique (must check before adding to DB)
     * @param name the name to check
     * @throws ResponseStatusException 409 if there already exists an ingredient with that name
     */
    private void checkNameUniqueness(String name) {
        if (ingredientRepository.existsByName(name))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ingredient name must be unique");
    }
}
