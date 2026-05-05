package dev.anhduc.bookgiftshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestChangePasswordDTO {
    @NotBlank(message = "Email không được để trống!")
    private String email;
    @NotBlank(message = "Password không được để trống!")
    private String password;

    @NotBlank(message = "Password mới không được để trống!")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}
