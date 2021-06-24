package fh.server.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "PIZZA")
public class Pizza implements Serializable {

    @Id
    @GeneratedValue
    private Long id; // used for most identification procedures

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String desc;

    @ManyToMany(targetEntity = Ingredient.class)
    private final Set<Ingredient> ingredients = new HashSet<>();

    @Column(nullable = false)
    private Integer price = 15;

    @Column(nullable = false)
    private Boolean available;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        for (Ingredient ingredient : ingredients) if (!ingredient.getAvailable()) return false;
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
