package dev.anhduc.bookgiftshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import dev.anhduc.bookgiftshop.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {
    List<Promotion> findByIdIn(List<Long> id);

    Promotion findByCode(String code);
}
