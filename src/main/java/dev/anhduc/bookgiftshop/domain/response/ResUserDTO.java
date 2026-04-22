package dev.anhduc.bookgiftshop.domain.response;

import java.time.Instant;

import dev.anhduc.bookgiftshop.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDTO {
    private Long id;
    private String email;
    private String fullName;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;
    private RoleUser role;

    @Setter
    @Getter
    public static class RoleUser {
        private Long id;
        private String name;
    }
}
