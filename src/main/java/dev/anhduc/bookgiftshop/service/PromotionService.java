package dev.anhduc.bookgiftshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.response.ResCreatePromotionDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdatePromotionDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Product;
import dev.anhduc.bookgiftshop.entity.Promotion;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.repository.ProductRepository;
import dev.anhduc.bookgiftshop.repository.PromotionRepository;
import jakarta.transaction.Transactional;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public PromotionService(PromotionRepository promotionRepository, ProductRepository productRepository) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    public ResCreatePromotionDTO createPromotion(Promotion promotion) throws IdInvalidException {
        if (this.promotionRepository.findByCode(promotion.getCode()) != null) {
            throw new IdInvalidException("Promotion Code đã tồn tại!");
        }
        if (promotion.getDiscountPercent() > 100) {
            throw new IdInvalidException("Mức giảm giá phải <= 100%!");
        }
        promotion = this.promotionRepository.save(promotion);
        ResCreatePromotionDTO res = new ResCreatePromotionDTO();
        res.setId(promotion.getId());
        res.setName(promotion.getName());
        res.setDiscountPercent(promotion.getDiscountPercent());
        res.setCode(promotion.getCode());
        res.setActive(promotion.getActive());
        res.setCreatedAt(promotion.getCreatedAt());
        res.setCreatedBy(promotion.getCreatedBy());
        res.setEndDate(promotion.getEndDate());
        res.setStartDate(promotion.getStartDate());
        return res;
    }

    public Promotion fetchPromotionById(Long id) throws IdInvalidException {
        Promotion promotion = this.promotionRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Promotion với id = " + id + " không tồn tại!"));
        return promotion;
    }

    public ResultPaginationDTO fetchAllPromotions(Specification<Promotion> specification, Pageable pageable) {
        Page<Promotion> promotionPage = this.promotionRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(promotionPage.getTotalPages());
        meta.setTotal(promotionPage.getTotalElements());
        result.setMeta(meta);
        result.setResult(promotionPage.getContent());
        return result;
    }

    public ResUpdatePromotionDTO updatePromotionById(Long id, Promotion requesPromotion) throws IdInvalidException {
        Promotion promotion = this.fetchPromotionById(id);
        if (this.promotionRepository.findByCode(requesPromotion.getCode()) != null) {
            throw new IdInvalidException("Promotion Code đã tồn tại!");
        }
        if (requesPromotion.getDiscountPercent() > 100) {
            throw new IdInvalidException("Mức giảm giá phải <= 100%!");
        }

        promotion.setActive(requesPromotion.getActive());
        promotion.setCode(requesPromotion.getCode());
        promotion.setDiscountPercent(requesPromotion.getDiscountPercent());
        promotion.setEndDate(requesPromotion.getEndDate());
        promotion.setName(requesPromotion.getName());
        promotion.setStartDate(requesPromotion.getStartDate());
        promotion.setEndDate(requesPromotion.getEndDate());
        promotion = this.promotionRepository.save(promotion);
        ResUpdatePromotionDTO res = new ResUpdatePromotionDTO();
        res.setId(promotion.getId());
        res.setDiscountPercent(promotion.getDiscountPercent());
        res.setStartDate(promotion.getStartDate());
        res.setEndDate(promotion.getEndDate());
        res.setName(promotion.getName());
        res.setUpdatedBy(promotion.getUpdatedBy());
        res.setUpdatedAt(promotion.getUpdatedAt());
        return res;
    }

    @Transactional
    public void deletePromotionById(Long id) throws IdInvalidException {
        Promotion promotion = this.fetchPromotionById(id);

        // 1. Tìm tất cả sản phẩm đang chứa promotion này
        List<Product> products = this.productRepository.findByPromotions(promotion);

        if (products != null) {
            for (Product product : products) {
                // Chỉ cần remove object promotion khỏi list của product
                // Hibernate/JPA sẽ tự xử lý xóa ở bảng trung gian
                product.getPromotions().remove(promotion);
            }
            // Lưu hàng loạt (Batch save) - Thực tế trong Transaction
            // bạn thậm chí không cần gọi save() thủ công nếu list đã được thay đổi
            this.productRepository.saveAll(products);
        }

        promotion.setDeleted(true);
        this.promotionRepository.save(promotion);
    }
}
