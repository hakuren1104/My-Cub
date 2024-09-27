package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private Long id;
    private String itemNm;
    private String cardColor;
    private String cardValue;
    private String cardSeries;
    private String itemDetail;
}
