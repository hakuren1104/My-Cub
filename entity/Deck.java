package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Deck {

    @Id
    @Column(name="deck_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String writer; //작성자

    private String name; // 덱 이름

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "deck_id")
    private List<Card> cards = new ArrayList<>();

    // 추가적인 필드와 메서드

    public void addCard(Card card) {
        cards.add(card);
    }

    // 카드 제거 메서드
    public void removeCard(Card card) {
        cards.remove(card);
    }
}
