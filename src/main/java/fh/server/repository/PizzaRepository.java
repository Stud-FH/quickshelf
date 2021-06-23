package fh.server.repository;

import fh.server.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pizzaRepository")
public interface PizzaRepository extends JpaRepository<Pizza, String> {

}
