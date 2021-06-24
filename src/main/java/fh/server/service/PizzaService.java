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
public class PizzaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final PizzaRepository pizzaRepository;

    public PizzaService(
            @Qualifier("pizzaRepository") PizzaRepository pizzaRepository
    ) {
        this.pizzaRepository = pizzaRepository;
    }

    /**
     * fetches all pizzas from the repository
     * @return list of all known pizzas
     */
    public Collection<Pizza> getPizzas() {
        return pizzaRepository.findAll();
    }

    /**
     * creates a new pizza entity
     * @param blueprint pizza blueprint
     * @param account caller
     * @return created pizza
     */
    public Pizza createPizza(Pizza blueprint, Account account) {
        checkNameFormat(blueprint.getName());
        checkNameUniqueness(blueprint.getName());
        checkDescFormat(blueprint.getDesc());
        checkPrizeConstraints(blueprint.getPrice());
        if (blueprint.getAvailable() == null) blueprint.setAvailable(true);
        Pizza created = pizzaRepository.saveAndFlush(blueprint);
        LOGGER.info(account.getEmail() + " created pizza " + created.getName());
        return created;
    }

    /**
     * updates a pizza
     * @param id pizza to update
     * @param blueprint attributes to update
     * @param account caller
     * @return updated pizza
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this update;
     *          404 if no pizza is found;
     *          In case of an exception, none of the updates are performed.
     */
    public Pizza updatePizza(Long id, Pizza blueprint, Account account) {
        if (!pizzaRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid pizza id");

        if (blueprint.getName() != null) {
            checkNameFormat(blueprint.getName());
            checkNameUniqueness(blueprint.getName());
        }
        if (blueprint.getDesc() != null) {
            checkDescFormat(blueprint.getDesc());
        }
        if (blueprint.getPrice() != null) {
            checkPrizeConstraints(blueprint.getPrice());
        }

        Pizza pizza = pizzaRepository.getById(id);
        if (blueprint.getName() != null) {
            pizza.setName(blueprint.getName());
        }
        if (blueprint.getDesc() != null) {
            pizza.setDesc(blueprint.getDesc());
        }
        if (blueprint.getIngredients() != null) {
            pizza.setIngredients(blueprint.getIngredients());
        }
        if (blueprint.getPrice() != null) {
            pizza.setPrice(blueprint.getPrice());
        }
        if (blueprint.getAvailable() != null) {
            pizza.setAvailable(blueprint.getAvailable());
        }
        pizzaRepository.flush();
        LOGGER.info(account.getEmail() + " updated pizza " + pizza.getName());
        return pizza;
    }

    /**
     * checks if a String is a valid pizza name
     * @param name the String to check
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this name
     */
    private void checkNameFormat(String name) {
        if (name == null || name.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name must be defined");
    }

    /**
     * checks if a String is a valid pizza description
     * @param desc the String top check
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this description
     */
    private void checkDescFormat(String desc) {
        if (desc.length() >= 255)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "description too long");
    }

    /**
     * checks if an Integer is a valid prize
     * @param prize the String top check
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this prize
     */
    private void checkPrizeConstraints(Integer prize) {
        if (prize == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "prize cannot be null");
        if (prize < 0)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "prize cannot be negative");
    }

    /**
     * checks whether a pizza name is unique (must check before adding to DB)
     * @param name the name to check
     * @throws ResponseStatusException 409 if there already exists a pizza with that name
     */
    private void checkNameUniqueness(String name) {
        if (pizzaRepository.existsByName(name))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pizza name must be unique");
    }
}
