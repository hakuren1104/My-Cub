package com.shop.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.dto.FreeImgDto;
import com.shop.dto.FreePostFormDto;
import com.shop.dto.ShopImgDto;
import com.shop.dto.ShopWriteFormDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Entity //db에 저장
@Table(name="FreePost")
@Getter
@Setter
@ToString
public class FreePost {

    @Id
    @Column(name="free_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 12)
    private String password;

    @OneToMany(mappedBy = "freePost", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FreeImg> freeImgs;

    @Column(nullable = false,length = 50)
    private String nickName;

    @Column(nullable = false,length = 50)
    private String title;

    @OneToMany(mappedBy = "freePost", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    @JsonManagedReference
    private List<Comment> comments;

    @Column(nullable = false)
    private String content;

    public FreePostFormDto toDTO() {
        FreePostFormDto dto = new FreePostFormDto();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setPassword(this.password);
        dto.setContent(this.content);
        dto.setNickName(this.nickName);

        // ShopImg 리스트를 ShopImgDto 리스트로 변환하여 DTO에 추가
        List<FreeImgDto> freeImgDtoList = this.freeImgs.stream()
                .map(img -> {
                    FreeImgDto freeImgDto = new FreeImgDto();
                    freeImgDto.setId(img.getId());
                    freeImgDto.setImgName(img.getImgName());
                    freeImgDto.setOriImgName(img.getOriImgName());
                    freeImgDto.setImgUrl(img.getImgUrl());
                    freeImgDto.setRepImgYn(img.getRepImgYn());
                    return freeImgDto;
                })
                .collect(Collectors.toList());

        dto.setFreeImgDtoList(freeImgDtoList); // ShopImgDto 리스트를 DTO에 설정

        return dto;
    }



}
