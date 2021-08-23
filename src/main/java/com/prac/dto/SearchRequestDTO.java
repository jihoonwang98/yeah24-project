package com.prac.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class SearchRequestDTO {

    @NotBlank
    private String type;

    @NotBlank
    private String keyword;
}