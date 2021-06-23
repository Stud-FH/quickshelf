package fh.server.controller;

import fh.server.constant.OrderStatus;
import fh.server.rest.dto.OrderDTO;
import fh.server.service.OrderService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderDTO placeOrder(OrderDTO orderDTO) {
        // TODO
        return null;
    }

    public OrderDTO getOrder(Long id) {
        // TODO
        return null;
    }

    public Collection<OrderDTO> getAllOrders(String token, Optional<OrderStatus> status, Optional<String> accountname) {
        // TODO
        return null;
    }


}
