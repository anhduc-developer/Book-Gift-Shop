package dev.anhduc.bookgiftshop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestUpdateCartItemDTO {
    private Long cartItemId;
    private Long quantity;
}
