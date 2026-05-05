package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import dev.anhduc.bookgiftshop.dto.request.RequestOrderDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.dto.response.orderDTO.ResCreateOrderDTO;
import dev.anhduc.bookgiftshop.dto.response.orderDTO.ResOrderDTO;
import dev.anhduc.bookgiftshop.entity.Cart;
import dev.anhduc.bookgiftshop.entity.Order;
import dev.anhduc.bookgiftshop.entity.User;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.CartService;
import dev.anhduc.bookgiftshop.service.OrderService;
import dev.anhduc.bookgiftshop.service.UserService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PostMapping("/orders")
    @ApiMessage("Create New Order")
    public ResponseEntity<ResCreateOrderDTO> createOrder(@Valid @RequestBody RequestOrderDTO orderDTO)
            throws IdInvalidException {
        User currentUser = this.userService.getCurrentUserLogin();
        Cart cart = this.cartService.getOrCreateCart(currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.orderService.createOrder(currentUser, cart, orderDTO));
    }

    @GetMapping("/orders/{id}")
    @ApiMessage("Fetch Order By Id")
    public ResponseEntity<ResOrderDTO> fetchOrderById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok().body(this.orderService.fetchOrderById(id));
    }

    @GetMapping("/orders")
    @ApiMessage("Fetch All Orders")
    public ResponseEntity<ResultPaginationDTO> fetchAllOrders(@Filter Specification<Order> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.orderService.fetchAllOrders(specification, pageable));
    }

    @DeleteMapping("/orders/{id}")
    @ApiMessage("Delete Order By Id")
    public ResponseEntity<Void> deleteOrderById(@PathVariable("id") Long id) throws IdInvalidException {
        this.orderService.deleteOrderById(id);
        return ResponseEntity.ok().body(null);
    }
}
