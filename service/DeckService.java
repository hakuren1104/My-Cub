package com.shop.service;


import com.shop.dto.CardDto;
import com.shop.dto.CardSearchDto;
import com.shop.dto.DeckSearchDto;
import com.shop.dto.ShopWriteFormDto;
import com.shop.entity.Card;
import com.shop.entity.Deck;
import com.shop.entity.Item;
import com.shop.repository.DeckCustom;
import com.shop.repository.DeckRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final ItemRepository itemRepository;
    private final DeckCustom deckCustom;


    @Transactional
    public void saveDeck(String deckName, String deckWriter,String deckPassword, List<CardDto> cardDtos) {
        // 새 덱을 생성하고 이름을 설정합니다.
        Deck deck = new Deck();

        deck.setWriter(deckWriter);
        deck.setName(deckName);
        deck.setPassword(deckPassword);

        // 카드 DTO 리스트를 순회하면서 카드 객체를 생성하고 덱에 추가합니다.
        for (CardDto cardDto : cardDtos) {
            // 카드 객체를 생성합니다.
            Card card = new Card();
            card.setImgUrl(cardDto.getImgUrl()); // 카드 이미지 URL 설정
            card.setName(cardDto.getName()); // 카드 이름 설정
            card.setColor(cardDto.getColor()); // 카드 색상 설정
            card.setSeries(cardDto.getSeries()); // 카드 시리즈 설정
            card.setCount(String.valueOf(cardDto.getCount())); // 카드 수량 설정 (String으로 변환)

            // 덱에 카드 추가
            deck.addCard(card); // Deck의 addCard 메서드를 사용하여 추가
        }

        // 덱을 데이터베이스에 저장합니다.
        deckRepository.save(deck);
    }


    @Transactional(readOnly = true)
    public Page<Deck> getAllDecks(DeckSearchDto deckSearchDto, Pageable pageable){
        return deckCustom.getAllDecks(deckSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Deck getDeckById(Long id) {
        return deckRepository.findByIdWithCards(id)
                .orElseThrow(() -> new RuntimeException("Deck not found with id " + id));
    }

    public void deleteDeckById(Long deckId) {
        if (deckRepository.existsById(deckId)) {
            deckRepository.deleteById(deckId);
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }
}
