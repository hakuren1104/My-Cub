package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType; //조회 날짜

    private String searchBy; //조회 유형

    private String searchQuery = ""; //검색 단어

    private String AuthorEmail;

}
