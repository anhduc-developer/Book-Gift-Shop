package dev.anhduc.bookgiftshop.domain.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResCreateCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
    private String createdBy;

}
