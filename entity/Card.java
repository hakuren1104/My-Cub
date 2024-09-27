package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Card {
    @Id
    @Column(name="card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; // 카드 이름
    @Column
    private String color; // 카드 색상
    @Column
    private String series; // 카드 시리즈
    @Column
    private String imgUrl; // 카드 이미지 URL
    @Column
    private String count; // 카드 수량
}
