package dev.anhduc.bookgiftshop.dto.request;

import dev.anhduc.bookgiftshop.utils.constants.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUpdateOrderDTO {
    private OrderStatusEnum status;
}
