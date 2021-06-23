package fh.server.entity;

import fh.server.constant.OrderStatus;

import java.util.List;

public class Order {

    private Long id;

    private String address;

    private String phoneNumber;

    private List<Pizza> pizzas;

    private OrderStatus status;

}
