package dev.anhduc.bookgiftshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import dev.anhduc.bookgiftshop.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
