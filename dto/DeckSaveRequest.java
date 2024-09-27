package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeckSaveRequest {

    private String deckWriter;

    private String deckPassword;

    private String deckName;

    private List<CardDto> cards;
}
