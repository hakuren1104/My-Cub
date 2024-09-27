package com.shop.repository;


import com.shop.dto.DeckSearchDto;
import com.shop.entity.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DeckCustom {
    Page<Deck> getAllDecks(DeckSearchDto deckSearchDto, Pageable pageable);
}
