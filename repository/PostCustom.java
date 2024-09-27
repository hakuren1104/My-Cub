package com.shop.repository;


import com.shop.dto.CardSearchDto;
import com.shop.dto.ShopWriteFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCustom {
    Page<ShopWriteFormDto> getAllpage(CardSearchDto cardSearchDto, Pageable pageable);
}
