package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Instant updatedAt;
    private String updatedBy;
}
