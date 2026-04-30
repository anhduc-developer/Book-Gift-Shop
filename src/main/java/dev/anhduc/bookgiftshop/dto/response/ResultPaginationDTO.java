package dev.anhduc.bookgiftshop.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

    @Getter
    @Setter

    public static class Meta {
        private int page; // page
        private int pageSize; // pageSize
        private int pages; // total page
        private Long total; // total element
    }
}
