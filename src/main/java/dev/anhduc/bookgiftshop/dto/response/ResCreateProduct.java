package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResCreateProduct {
    private Long id;
    private String name;
    private Long stockQuantity;
    private Long sold;
    private String shortDescription;
    private String detailDescription;
    private Instant createdAt;
    private String createdBy;
    private String photo;
    private List<String> authors;
    private String publisher;
    private List<String> categories;
    private List<String> promotions;
}
