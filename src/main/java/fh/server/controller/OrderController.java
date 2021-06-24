package fh.server.controller;

import fh.server.constant.OrderStatus;
import fh.server.entity.Account;
import fh.server.entity.Order;
import fh.server.rest.dto.OrderDTO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.service.AccountService;
import fh.server.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final AccountService accountService;

    public OrderController(OrderService orderService, AccountService accountService) {
        this.orderService = orderService;
        this.accountService = accountService;
    }

    /**
     * creates a new order
     * @param orderDTO order blueprint
     * @param id customer id
     * @param token authentication token
     * @return created order
     */
    @PostMapping("/orders/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrderDTO createOrder(
            @RequestBody OrderDTO orderDTO,
            @RequestHeader("id") Long id,
            @RequestHeader("token") String token
    ) {
        Account customer = accountService.authenticateAccount(id, token, Account.CLEARANCE_LEVEL_CUSTOMER);
        Order blueprint = DTOMapper.INSTANCE.convertOrderDTOtoEntity(orderDTO);
        Order created = orderService.createOrder(blueprint, customer);
        return DTOMapper.INSTANCE.convertEntityToOrderDTO(created);
    }

    /**
     * fetches an order from the database
     * @param accountId requester id
     * @param token authentication token
     * @param orderId order id
     * @return found order
     */
    @GetMapping("/orders/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public OrderDTO getOrder(
            @RequestHeader("accountId") Long accountId,
            @RequestHeader("token") String token,
            @RequestHeader("orderId") Long orderId
    ) {
        Account requester = accountService.authenticateAccount(accountId, token, Account.CLEARANCE_LEVEL_CUSTOMER);
        Order order = orderService.getOrder(orderId, requester);
        return DTOMapper.INSTANCE.convertEntityToOrderDTO(order);
    }

    /**
     * fetches all orders from the database.
     * exclusive to employees.
     * can filter by:
     * - status
     * - customerId
     * @param accountId requester id
     * @param token authentication token
     * @param status order status filter
     * @param customerId order customerId filter
     * @return list of found orders
     */
    @GetMapping("/orders/all")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<OrderDTO> getAllOrders(
            @RequestHeader("accountId") Long accountId,
            @RequestHeader("token") String token,
            @RequestParam("status") Optional<OrderStatus> status,
            @RequestParam("customerId") Optional<Long> customerId
    ) {
        Account requester = accountService.authenticateAccount(accountId, token, Account.CLEARANCE_LEVEL_EMPLOYEE);
        Collection<Order> orders = orderService.getAllOrders();
        Collection<OrderDTO> list = orders.stream().map(DTOMapper.INSTANCE::convertEntityToOrderDTO).collect(Collectors.toList());
        if (status.isPresent()) list = list.stream().filter(e -> e.getStatus() == status.get()).collect(Collectors.toList());
        if (customerId.isPresent()) list = list.stream().filter(e -> e.getClientId().equals(customerId.get())).collect(Collectors.toList());
        return list;
    }

    /**
     * updates an order.
     * only employees can update orders and they can only update the status and comment.
     * @param orderDTO update blueprint
     * @param accountId requester id
     * @param token authentication token
     * @param orderId order to update
     * @return updated order
     */
    @PutMapping("/orders/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public OrderDTO updateOrder(
            @RequestBody OrderDTO orderDTO,
            @RequestHeader("accountId") Long accountId,
            @RequestHeader("token") String token,
            @RequestHeader("orderId") Long orderId
    ) {
        Account employee = accountService.authenticateAccount(accountId, token, Account.CLEARANCE_LEVEL_EMPLOYEE);
        Order blueprint = DTOMapper.INSTANCE.convertOrderDTOtoEntity(orderDTO);
        Order updated = orderService.updateOrder(orderId, blueprint, employee);
        return DTOMapper.INSTANCE.convertEntityToOrderDTO(updated);
    }

}
