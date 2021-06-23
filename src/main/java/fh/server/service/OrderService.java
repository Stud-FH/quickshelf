package fh.server.service;

import fh.server.entity.Order;
import fh.server.repository.OrderRepository;
import fh.server.rest.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(
            @Qualifier("orderRepository") OrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }



    public Order placeOrder(Order blueprint) {
        // TODO
        return null;
    }

    public Order getOrder(Long id) {
        // TODO
        return null;
    }

    public Collection<Order> getAllOrders() {
        // TODO
        return null;
    }
}
