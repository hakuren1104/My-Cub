package com.shop.controller;


import com.shop.dto.*;
import com.shop.entity.Card;
import com.shop.entity.Deck;
import com.shop.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    // ---------------------------------덱 저장---------------------------------------

    @PostMapping(value="/deck/save")
    public ResponseEntity<String> saveDeck(@RequestBody DeckSaveRequest request) {

        List<CardDto> deck = request.getCards();

        try {
            deckService.saveDeck(request.getDeckName(),request.getDeckWriter(), request.getDeckPassword(), deck);
            return ResponseEntity.ok("덱이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("덱 저장에 실패했습니다.");
        }
    }

    //-------------------------------덱 조회---------------------------------------

    @GetMapping("/deck/view/{id}")
    public String viewDeck(@PathVariable Long id, Model model) {
        // 덱을 가져옵니다.
        Deck deck = deckService.getDeckById(id);

        // 모델에 덱과 카드 정보를 추가합니다.
        model.addAttribute("deck", deck);
        model.addAttribute("cards", deck.getCards());

        return "card/deckView"; // 상세 정보를 보여줄 뷰 페이지로 이동합니다.
    }


    //----------------------------------덱 삭제----------------------------------------

    @PostMapping("/deck/deleteDeck")
    public String deletePost(
            @RequestParam("deckId") Long deckId,
            RedirectAttributes redirectAttributes, Optional<Integer> page, Model model) {

        try {
            // 게시글 삭제
            deckService.deleteDeckById(deckId);
            redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        } catch (Exception e) {
            // 삭제 중 오류 발생 시
            redirectAttributes.addFlashAttribute("error", "게시글 삭제에 실패했습니다.");
        }

        DeckSearchDto deckSearchDto = new DeckSearchDto();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Deck> decks = deckService.getAllDecks(deckSearchDto, pageable);

        model.addAttribute("decks", decks);
        model.addAttribute("deckSearchDto", deckSearchDto);
        model.addAttribute("maxPage", 50);

        return "card/deckBoardMain";
    }
}
