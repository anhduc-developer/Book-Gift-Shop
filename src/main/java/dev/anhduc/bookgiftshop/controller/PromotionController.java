package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.anhduc.bookgiftshop.domain.dto.response.ResCreatePromotionDTO;
import dev.anhduc.bookgiftshop.domain.entity.Promotion;
import dev.anhduc.bookgiftshop.service.PromotionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // @PostMapping("/promotions")
    // public ResponseEntity<ResCreatePromotionDTO> createPromotion(@RequestBody
    // Promotion promotion) {

    // }

}
