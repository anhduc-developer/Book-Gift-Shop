package dev.anhduc.bookgiftshop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestAddToCartDTO {
    private Long productId;
    private Long quantity;
}
