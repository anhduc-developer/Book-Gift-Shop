package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import dev.anhduc.bookgiftshop.dto.response.ResCreatePromotionDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdatePromotionDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Promotion;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.PromotionService;
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
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/admin")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping("/promotions")
    @ApiMessage("Create New Promotion")
    public ResponseEntity<ResCreatePromotionDTO> createPromotion(@Valid @RequestBody Promotion promotion)
            throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.promotionService.createPromotion(promotion));
    }

    @GetMapping("/promotions/{id}")
    @ApiMessage("Fetch Promotion By Id")
    public ResponseEntity<Promotion> fetchPromotionById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok().body(this.promotionService.fetchPromotionById(id));
    }

    @GetMapping("/promotions")
    @ApiMessage("Fetch Promotion By Id")
    public ResponseEntity<ResultPaginationDTO> fetchAllPromotions(@Filter Specification<Promotion> specification,
            Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok().body(this.promotionService.fetchAllPromotions(specification, pageable));
    }

    @PutMapping("/promotions/{id}")
    @ApiMessage("Update Promotion By Id")
    public ResponseEntity<ResUpdatePromotionDTO> updatePromotionById(@PathVariable("id") Long id,
            @RequestBody Promotion requesPromotion) throws IdInvalidException {
        return ResponseEntity.ok().body(this.promotionService.updatePromotionById(id, requesPromotion));
    }

    @DeleteMapping("/promotions/{id}")
    @ApiMessage("Delete Promotion By Id")
    public ResponseEntity<Void> deletePromotionById(@PathVariable("id") Long id) throws IdInvalidException {
        this.promotionService.deletePromotionById(id);
        return ResponseEntity.ok().body(null);
    }
}
