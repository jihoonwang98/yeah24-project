package com.prac.dto;

import lombok.Data;

import java.util.List;

public class CartDTO {

    @Data
    public static class CartInfoDTO {
        private Long bookId;
        private int amount;
        private int priceSum;
    }


    @Data
    public static class CartInfoListDTO {
        List<CartInfoDTO> books;
    }
}
