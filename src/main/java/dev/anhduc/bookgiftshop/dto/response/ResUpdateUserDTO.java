package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;

import dev.anhduc.bookgiftshop.utils.constants.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
    private Long id;
    private String email;
    private String fullname;
    private int age;
    private String address;
    private String phoneNumber;
    private GenderEnum gender;
    private Instant updatedAt;
}
