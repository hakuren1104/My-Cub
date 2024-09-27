package com.shop.dto;


import com.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private  Long id;

    @NotBlank(message = "카드명을 입력하세요!")
    private String itemNm;

    @NotNull(message = "컬러를 지정하세요!")
    private String cardColor;

    @NotNull(message = "카드 종류를 지정하세요!")
    private String cardValue;

    @NotNull(message = "확장팩을 정해주세요!")
    private String cardSeries;

    @NotBlank(message = "쿠키 정보를 작성하세요!")
    private String itemDetail;


    //-------------------------------------------------------------------
    //ItemImg

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 이미지 정보

    private List<Long> itemImgIds = new ArrayList<>(); //상품 이미지 아이디

    //--------------------------------------------------------------------
    // ModelMapper
    private static ModelMapper modelMapper =new ModelMapper();

    public Item createItem(){
        //ItemDto -> Item 연결
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item){ //수정하기 위한 모델맵퍼 연결
        //Item -> ItemDto 연결
        return modelMapper.map(item,ItemFormDto.class);
    }
}
