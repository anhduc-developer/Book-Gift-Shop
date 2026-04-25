package dev.anhduc.bookgiftshop.service;

import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.repository.PromotionRepository;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

}
