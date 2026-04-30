package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ResCreatePromotionDTO {
    private Long id;
    private String name;

    private Double discountPercent;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
    private Instant createdAt;
    private String createdBy;
}
