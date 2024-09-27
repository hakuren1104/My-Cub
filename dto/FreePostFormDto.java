package com.shop.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.shop.entity.FreePost;
import com.shop.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FreePostFormDto {

    private Long id;

    @NotBlank(message = "비밀번호를 입력하세요!")
    private String password;

    @NotBlank(message = "작성자를 입력하세요!")
    private String nickName;

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "내용을 작성해주세요!")
    private String content;

    //---------------shopImg---------------------------

    private List<FreeImgDto> freeImgDtoList = new ArrayList<>();

    private List<Long> freePostImgIds = new ArrayList<>(); //상품 이미지 아이디

    //--------------------------------------------------------------------
    // ModelMapper
    private static ModelMapper modelMapper =new ModelMapper();

    public FreePost createFreePost(){
        //ItemDto -> Item 연결
        return modelMapper.map(this, FreePost.class);
    }
    public static FreePostFormDto of(FreePost freePost){ //수정하기 위한 모델맵퍼 연결
        //Item -> ItemDto 연결
        return modelMapper.map(freePost,FreePostFormDto.class);
    }


}
