package dev.anhduc.bookgiftshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRegiterDTO {
    @NotBlank(message = "Email không được để trống!")
    private String email;
    @NotBlank(message = "Password không được để trống!")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    private String phoneNumber;
    private int age;
    private String fullName;
}
