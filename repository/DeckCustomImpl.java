package com.shop.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.CardSearchDto;
import com.shop.dto.DeckSearchDto;
import com.shop.dto.ShopWriteFormDto;
import com.shop.entity.Deck;
import com.shop.entity.QCard;
import com.shop.entity.QDeck;
import com.shop.entity.QPost;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class DeckCustomImpl implements DeckCustom {

    private final JPAQueryFactory queryFactory;

    public DeckCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Deck> getAllDecks (DeckSearchDto deckSearchDto, Pageable pageable) {

        QDeck deck = QDeck.deck;
        QCard card = QCard.card;

        // QueryDSL에서 사용할 조건 생성
        BooleanBuilder builder = new BooleanBuilder();

        // Deck 정보 조회
        List<Deck> decks = queryFactory
                .selectFrom(deck)
                .leftJoin(deck.cards, card) // 카드와 연관된 정보를 가져옵니다.
                .fetchJoin() // 연관된 카드들도 함께 가져옵니다.
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 데이터 수 조회
        long total = queryFactory
                .selectFrom(deck)
                .leftJoin(deck.cards, card)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(decks, pageable, total);
    }
}
