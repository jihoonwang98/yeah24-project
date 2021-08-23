package com.prac.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PageRequestDTO {

    public static final int DEFAULT_SIZE = 3;

    private int size;
    private int page;
    private String orderBy;

    public PageRequestDTO() {
        this(DEFAULT_SIZE, 1);
    }

    public PageRequestDTO(int size, int page) {
        this.size = size;
        this.page = page;
    }

    public Pageable of() {
        return PageRequest.of(this.page - 1, this.size);
    }
}
