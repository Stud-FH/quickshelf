package fh.server.rest.dto;

import fh.server.entity.Ingredient;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class PizzaDTO {

    private Long id;

    private String name;

    private String desc;

    private Set<Long> ingredients;

    private Integer price;

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

    public Set<Long> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients.stream().map(Ingredient::getId).collect(Collectors.toSet());
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
