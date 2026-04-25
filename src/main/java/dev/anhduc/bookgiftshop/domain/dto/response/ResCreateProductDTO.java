package dev.anhduc.bookgiftshop.domain.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResCreateProductDTO {
    private Long id;
    private String name;
    private Long stockQuantity;
    private Long sold;
    private String shortDescription;
    private String detailDescription;
    private Instant createdAt;
    private String createdBy;
    private boolean deleted;
    private String photo;
}
