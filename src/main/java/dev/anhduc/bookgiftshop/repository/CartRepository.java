package dev.anhduc.bookgiftshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import dev.anhduc.bookgiftshop.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByUserId(Long id);
}
