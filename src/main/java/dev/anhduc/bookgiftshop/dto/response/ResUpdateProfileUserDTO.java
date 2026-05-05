package dev.anhduc.bookgiftshop.dto.response;

import dev.anhduc.bookgiftshop.utils.constants.GenderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateProfileUserDTO {
    private String fullName;
    private int age;
    private String address;
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;
    private String avatar;
}
