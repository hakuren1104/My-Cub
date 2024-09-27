package com.shop.repository;


import com.shop.dto.FreePostFormDto;
import com.shop.dto.FreePostSearchDto;
import com.shop.dto.MainFreePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreePostCustom {
    Page<MainFreePostDto> getAllpage(FreePostSearchDto freePostSearchDto, Pageable pageable);
}
