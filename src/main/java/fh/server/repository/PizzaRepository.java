package fh.server.repository;

import fh.server.entity.Ingredient;
import fh.server.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository("pizzaRepository")
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    boolean existsByName(String name);
    Collection<Pizza> findAllByIngredientsContaining(Ingredient ingredient);
}
