package com.shop.dto;

import com.shop.entity.Item;
import com.shop.entity.Post;
import com.shop.entity.ShopImg;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShopWriteFormDto {

    private Long id;

    @NotBlank(message = "작성자를 입력하세요!")
    private String nickName;

    @NotBlank(message = "비밀번호를 입력하세요!")
    private String password;

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotNull(message = "거래 종류를 입력하세요!")
    private String trade;

    @NotBlank(message = "내용을 작성해주세요!")
    private String content;

    //---------------shopImg---------------------------

    private List<ShopImgDto> shopImgDtoList = new ArrayList<>();

    private List<Long> shopImgIds = new ArrayList<>(); //상품 이미지 아이디


    //--------------------------------------------------------------------
    // ModelMapper
    private static ModelMapper modelMapper =new ModelMapper();

    public Post createPost(){
        //ItemDto -> Item 연결
        return modelMapper.map(this, Post.class);
    }
    public static ShopWriteFormDto of(Post post){ //수정하기 위한 모델맵퍼 연결
        //Item -> ItemDto 연결
        return modelMapper.map(post,ShopWriteFormDto.class);
    }


}
