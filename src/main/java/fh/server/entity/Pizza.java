package fh.server.entity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Pizza implements Serializable {

    @Id
    private String name;

    @Column
    private String desc;

    @ElementCollection
    private Collection<String> ingredients;

    @Column(nullable = false)
    private Integer price = 15;

    @Column(nullable = false)
    private Boolean available;


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

    public Collection<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<String> ingredients) {
        this.ingredients = ingredients;
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
