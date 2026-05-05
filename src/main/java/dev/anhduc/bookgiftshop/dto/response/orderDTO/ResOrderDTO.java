package dev.anhduc.bookgiftshop.dto.response.orderDTO;

import java.time.Instant;

import dev.anhduc.bookgiftshop.utils.constants.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResOrderDTO {
    private Long id;
    private double totalPrice;
    private double discount;
    private double finalPrice;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private OrderStatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
}
