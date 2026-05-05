package dev.anhduc.bookgiftshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.response.ResCartDTO;
import dev.anhduc.bookgiftshop.entity.Cart;
import dev.anhduc.bookgiftshop.entity.CartItem;
import dev.anhduc.bookgiftshop.entity.Product;
import dev.anhduc.bookgiftshop.entity.User;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.repository.CartItemRepository;
import dev.anhduc.bookgiftshop.repository.CartRepository;
import dev.anhduc.bookgiftshop.repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
            ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart getOrCreateCart(User user) {
        Optional<Cart> cartOptional = this.cartRepository.findByUserId(user.getId());
        if (cartOptional.isPresent()) {
            return cartOptional.get();
        }
        Cart cart = new Cart();
        cart.setUser(user);
        return this.cartRepository.save(cart);
    }

    public ResCartDTO convertCartDTO(Cart cart) {
        ResCartDTO res = new ResCartDTO();
        res.setId(cart.getId());
        ResCartDTO.ResProduct resProduct = new ResCartDTO.ResProduct();
        List<ResCartDTO.ResPromotionDTO> promotions = new ArrayList<>();
        List<ResCartDTO.ResCartItem> cartItems = new ArrayList<>();
        if (cart.getCartItems() != null) {
            cart.getCartItems().forEach(u -> {
                ResCartDTO.ResCartItem resCartItem = new ResCartDTO.ResCartItem();
                Product product = u.getProduct();
                resProduct.setId(product.getId());
                resProduct.setName(product.getName());
                resProduct.setPrice(product.getPrice());
                product.getPromotions().forEach(v -> {
                    ResCartDTO.ResPromotionDTO resPromotion = new ResCartDTO.ResPromotionDTO();
                    resPromotion.setCode(v.getCode());
                    resPromotion.setDiscountPercent(v.getDiscountPercent());
                    resPromotion.setName(v.getName());
                    promotions.add(resPromotion);
                });
                resProduct.setPromotions(promotions);
                resCartItem.setId(u.getId());
                resCartItem.setQuantity(u.getQuantity());
                resCartItem.setProduct(resProduct);
                cartItems.add(resCartItem);
            });
        }
        res.setCartItems(cartItems);
        return res;
    }

    @Transactional
    public void addProductToCart(User user, Long productId, Long quantity) throws IdInvalidException {
        if (quantity <= 0) {
            throw new IdInvalidException("Quantity must be > 0");
        }

        Cart cart = this.getOrCreateCart(user);
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new IdInvalidException("Product not found"));

        long stockQuantityProduct = product.getStockQuantity();
        Optional<CartItem> cartOptional = this.cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (cartOptional.isPresent()) {
            CartItem currentCartItem = cartOptional.get();
            if (currentCartItem.getQuantity() + quantity > stockQuantityProduct) {
                throw new IdInvalidException("Exceeding inventory levels!");
            }
            currentCartItem.setQuantity(currentCartItem.getQuantity() + quantity);
        } else {
            if (stockQuantityProduct < quantity) {
                throw new IdInvalidException("Out of stock!");
            }
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(quantity);
            this.cartItemRepository.save(newCartItem);
        }
    }

    public void updateCartItem(Long cartItemId, Long quantity, User user) throws IdInvalidException {
        CartItem cartItem = this.cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IdInvalidException("CartItem not found!"));
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IdInvalidException("You do not own this cart item");
        }
        if (quantity <= 0) {
            this.cartItemRepository.deleteById(cartItemId);
        } else {
            if (cartItem.getProduct().getStockQuantity() < quantity) {
                throw new IdInvalidException("Not enough stock!");
            }
            cartItem.setQuantity(quantity);
            this.cartItemRepository.save(cartItem);
        }
    }

    public void deleteCartIteam(Long cartItemId) {
        this.cartItemRepository.deleteById(cartItemId);
    }
}
