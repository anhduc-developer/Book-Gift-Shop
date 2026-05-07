package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateProductDTO {
    private Long id;
    private String name;
    private Long stockQuantity;
    private Long sold;
    private String shortDescription;
    private String detailDescription;
    private Instant updatedAt;
    private String updatedBy;
    private String photo;
    private double price;
    private List<AuthorDTO> authors;
    private PublishserDTO publisher;
    private List<CategoryDTO> categories;

    @Setter
    @Getter
    @AllArgsConstructor
    public static class AuthorDTO {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    public static class PublishserDTO {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class CategoryDTO {
        private Long id;
        private String name;
    }

}
