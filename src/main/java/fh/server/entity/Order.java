package fh.server.entity;

import fh.server.constant.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Order implements Serializable {

    @Id
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToMany(targetEntity = Pizza.class)
    private List<Pizza> pizzas;

    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PLACED;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
