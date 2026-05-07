package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResAuthorDTO {
    private Long id;
    private String name;
    private String birth;
    private String avatar;
    private String nationality;
    private String biography;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<ProductDTO> products;

    @Setter
    @Getter
    public static class ProductDTO {
        private Long id;
        private String name;
    }
}
