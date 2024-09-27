package com.shop.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ShopImgDto;
import com.shop.dto.ShopWriteFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="post")
@Getter
@Setter
@ToString
public class Post extends BaseEntity {

    @Id  //PK
    @Column(name="post_id") //칼럼 이름
    @GeneratedValue(strategy = GenerationType.AUTO) //PK auto로 증가
    private Long id; //포스트 번호

    @Column(nullable = false,length = 12)
    private String password;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShopImg> shopImgs;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    @JsonManagedReference
    private List<ShopComment> comments;


    @Column(nullable = false,length = 50) //not null 길이 50
    private String nickName; //작성자

    @Column(nullable = false,length = 50) //not null 길이 50
    private String title; //제목

    @Lob  //대용량 저장
    @Column(nullable = false) // not null
    private String trade; //거래 종류

    @Column(nullable = false)
    private String content; //내용


    public void updatePost(ShopWriteFormDto shopWriteFormDto){
        this.content = shopWriteFormDto.getContent();
    }

    public ShopWriteFormDto toDTO() {
        ShopWriteFormDto dto = new ShopWriteFormDto();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setPassword(this.password);
        dto.setContent(this.content);
        dto.setNickName(this.nickName);
        dto.setTrade(this.trade);

        // ShopImg 리스트를 ShopImgDto 리스트로 변환하여 DTO에 추가
        List<ShopImgDto> shopImgDtoList = this.shopImgs.stream()
                .map(img -> {
                    ShopImgDto imgDto = new ShopImgDto();
                    imgDto.setId(img.getId());
                    imgDto.setImgName(img.getImgName());
                    imgDto.setOriImgName(img.getOriImgName());
                    imgDto.setImgUrl(img.getImgUrl());
                    imgDto.setRepImgYn(img.getRepImgYn());
                    return imgDto;
                })
                .collect(Collectors.toList());

        dto.setShopImgDtoList(shopImgDtoList); // ShopImgDto 리스트를 DTO에 설정

        return dto;
    }



}
