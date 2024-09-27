package com.shop.dto;


import com.shop.entity.FreeImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class FreeImgDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    public static FreeImgDto of(FreeImg freeImg){
        return modelMapper.map(freeImg, FreeImgDto.class);
    }
}
