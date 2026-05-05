package dev.anhduc.bookgiftshop.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRegisterUserDTO {
    private String email;
    private String phoneNumber;
    private int age;
}
