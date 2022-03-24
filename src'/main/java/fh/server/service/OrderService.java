package fh.server.service;

import fh.server.constant.OrderStatus;
import fh.server.entity.Account;
import fh.server.entity.Order;
import fh.server.entity.Pizza;
import fh.server.repository.OrderRepository;
import fh.server.repository.PizzaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private static final int DELIVERY_COST = 5;
    private static final int FREE_DELIVERY_THRESHOLD = 40;

    private final OrderRepository orderRepository;
    private final PizzaRepository pizzaRepository;

    public OrderService(
            @Qualifier("orderRepository") OrderRepository orderRepository,
            @Qualifier("pizzaRepository") PizzaRepository pizzaRepository
    ) {
        this.orderRepository = orderRepository;
        this.pizzaRepository = pizzaRepository;
    }

    /**
     * fetches all orders from the repository
     * @return collection of all found orders
     */
    public Collection<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * creates an order
     * @param blueprint order blueprint
     * @param customer the customer that places the order
     * @return the created order
     */
    public Order createOrder(Order blueprint, Account customer) {
        blueprint.setId(null);
        blueprint.setCustomerId(customer.getId());
        blueprint.setAddress(customer.getAddress());
        blueprint.setPhoneNumber(customer.getPhoneNumber());
        checkPizzasConstraints(blueprint.getPizzas());
        blueprint.setPrice(calculatePrize(blueprint.getPizzas()));
        checkCommentFormat(blueprint.getComment());
        blueprint.setStatus(OrderStatus.CREATED);

        Order created = orderRepository.saveAndFlush(blueprint);
        LOGGER.info(customer.getEmail() + " created an order");
        return created;
    }

    /**
     * fetches an order
     * @param id order id
     * @param requester requester
     * @return the found order
     * @throws ResponseStatusException
     *          404 if order was not found;
     *          403 if the requester is not an employee and did not place this order;
     */
    public Order getOrder(Long id, Account requester) {
        if (!orderRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid order id");
        Order order = orderRepository.getById(id);
        if (order.getCustomerId().equals(requester)) return order;
        if (requester.getClearanceLevel() >= Account.CLEARANCE_LEVEL_EMPLOYEE) return order;
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "only employees can see other clients' orders");
    }

    /**
     * updates an order.
     * can only update an order's status and comment.
     * @param id order id
     * @param blueprint updates to be made
     * @param employee the employee requesting the updates
     * @return updated order
     * @throws ResponseStatusException 404 if order was not found
     */
    public Order updateOrder(Long id, Order blueprint, Account employee) {
        if (!orderRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid order id");
        if (blueprint.getComment() != null) {
            checkCommentFormat(blueprint.getComment());
        }
        Order order = orderRepository.getById(id);
        if (blueprint.getStatus() != null) {
            order.setStatus(blueprint.getStatus());
        }
        if (blueprint.getComment() != null) {
            order.setComment(blueprint.getComment());
        }
        orderRepository.flush();
        LOGGER.info(employee.getEmail() + " updated an order");
        return order;
    }

    /**
     * calculates the prize for a list of pizzas
     * @param pizzas pizza list
     * @return summed up prize of all the pizzas + delivery cost if free-delivery threshold not reached
     */
    private int calculatePrize(List<Pizza> pizzas) {
        int prize = 0;
        for (Pizza pizza : pizzas) prize += pizza.getPrice();
        if (prize < FREE_DELIVERY_THRESHOLD) prize += DELIVERY_COST;
        return prize;
    }

    /**
     * checks if a list of pizzas is valid
     * @param pizzas list of pizzas to check
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this list
     */
    private void checkPizzasConstraints(List<Pizza> pizzas) {
        if (pizzas.contains(null))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "invalid pizza");
    }

    /**
     * checks if a String is a valid comment
     * @param comment the String top check
     * @throws ResponseStatusException if any security- or integrity constraints are violated by this comment
     */
    private void checkCommentFormat(String comment) {
        if (comment == null) return;
        if (comment.length() >= 255)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "comment too long");
        // TODO check for possible code injection
    }
}
