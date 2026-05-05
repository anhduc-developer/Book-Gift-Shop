package dev.anhduc.bookgiftshop.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestOrderDTO {

    private String receiverName;
    private String receiverAddress;
    @Pattern(regexp = "^[0-9]{10}$", message = "SĐT không hợp lệ")
    private String receiverPhone;
}
