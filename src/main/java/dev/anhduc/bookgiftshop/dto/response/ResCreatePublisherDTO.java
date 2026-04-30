package dev.anhduc.bookgiftshop.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResCreatePublisherDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Instant createdAt;
    private String createdBy;
}
