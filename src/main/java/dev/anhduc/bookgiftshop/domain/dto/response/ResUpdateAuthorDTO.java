package dev.anhduc.bookgiftshop.domain.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateAuthorDTO {
    private Long id;
    private String name;
    private String birth;
    private String avatar;
    private String nationality;
    private String biography;
    private Instant updatedAt;
    private String updatedBy;
}
