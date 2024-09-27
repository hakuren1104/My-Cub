package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {

    private String id;
    private String name;
    private String color;
    private String series;
    private String imgUrl;
    private int count;
}
