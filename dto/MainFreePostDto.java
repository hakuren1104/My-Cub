package com.shop.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainFreePostDto {
    private Long id;
    private String nickName;
    private String title;
    private String content;
    private String imgUrl;

    @QueryProjection // Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록 활용
    public MainFreePostDto(Long id, String nickName, String title
            ,String content, String imgUrl){
        this.id = id;
        this.nickName = nickName;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }
}
