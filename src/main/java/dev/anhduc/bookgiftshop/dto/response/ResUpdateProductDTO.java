package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;
import java.util.List;

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
    private double price;
    private String photo;
    private List<String> authors;
    private String publisher;
    private List<String> categories;
    private List<String> promotions;
}
