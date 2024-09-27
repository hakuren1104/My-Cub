package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MainItemDto {
    private Long id;
    private String itemNm;
    private String cardColor;
    private String cardValue;
    private String cardSeries;
    private String itemDetail;
    private String imgUrl;


    @QueryProjection // Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록 활용
    public MainItemDto(Long id, String itemNm, String itemDetail
                       ,String cardColor, String cardValue, String cardSeries, String imgUrl){
        this.id = id;
        this.itemNm = itemNm;
        this.cardColor = cardColor;
        this.cardValue = cardValue;
        this.cardSeries = cardSeries;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
    }
}
