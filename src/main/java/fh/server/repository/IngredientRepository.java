package fh.server.repository;

import fh.server.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ingredientRepository")
public interface IngredientRepository extends JpaRepository<Ingredient, String> {

}
