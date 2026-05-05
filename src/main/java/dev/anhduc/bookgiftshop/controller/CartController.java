package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.anhduc.bookgiftshop.dto.request.RequestAddToCartDTO;
import dev.anhduc.bookgiftshop.dto.request.RequestUpdateCartItemDTO;
import dev.anhduc.bookgiftshop.dto.response.ResCartDTO;
import dev.anhduc.bookgiftshop.entity.Cart;
import dev.anhduc.bookgiftshop.entity.User;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.CartService;
import dev.anhduc.bookgiftshop.service.UserService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/carts/me")
    @ApiMessage("Get my cart")
    public ResponseEntity<ResCartDTO> getMyCart() throws IdInvalidException {

        User currentUser = this.userService.getCurrentUserLogin();

        Cart cart = cartService.getOrCreateCart(currentUser);

        return ResponseEntity.ok(this.cartService.convertCartDTO(cart));
    }

    @PostMapping("/carts/items")
    @ApiMessage("Add product to cart")
    public ResponseEntity<String> addToCart(
            @RequestBody RequestAddToCartDTO request) throws IdInvalidException {
        User currentUser = this.userService.getCurrentUserLogin();

        cartService.addProductToCart(currentUser, request.getProductId(), request.getQuantity());

        return ResponseEntity.ok("Added to cart");
    }

    @PutMapping("/carts/items")
    @ApiMessage("Update CartIteam")
    public ResponseEntity<String> updateCartIteam(@RequestBody RequestUpdateCartItemDTO request)
            throws IdInvalidException {
        User currentUser = this.userService.getCurrentUserLogin();
        this.cartService.updateCartItem(request.getCartItemId(), request.getQuantity(), currentUser);
        return ResponseEntity.ok().body("Updated");
    }

    @DeleteMapping("/carts/items/{cartItemId}")
    @ApiMessage("Remove cart item")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {

        this.cartService.deleteCartIteam(cartItemId);

        return ResponseEntity.ok().body(null);
    }
}
