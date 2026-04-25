package dev.anhduc.bookgiftshop.domain.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdatePublisherDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Instant updatedAt;
    private String updatedBy;
}
