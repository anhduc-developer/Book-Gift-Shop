package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ResCreatePromotionDTO {
    private Long id;
    private String code;
    private String name;
    private Double discountPercent;
    private Instant startDate;
    private Instant endDate;
    private Boolean active;
    private Instant createdAt;
    private String createdBy;
}
