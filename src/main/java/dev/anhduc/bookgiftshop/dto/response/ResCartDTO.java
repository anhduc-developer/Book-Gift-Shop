package dev.anhduc.bookgiftshop.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResCartDTO {
    private Long id;
    private List<ResCartItem> cartItems;

    @Setter
    @Getter
    public static class ResCartItem {
        private Long id;
        private long quantity;
        private ResProduct product;
    }

    @Getter
    @Setter
    public static class ResProduct {
        private Long id;
        private String name;
        private double price;
        private List<ResPromotionDTO> promotions;
    }

    @Getter
    @Setter
    public static class ResPromotionDTO {
        private String code;
        private double discountPercent;
        private String name;
    }
}
