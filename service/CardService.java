package com.shop.service;


import com.shop.dto.FreePostFormDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ShopWriteFormDto;
import com.shop.entity.*;
import com.shop.repository.CardRepository;
import com.shop.repository.FreePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class CardService {

    private final CardRepository cardRepository;
    private final ShopImgService shopImgService;
    private final FreePostRepository freePostRepository;
    private final FreeImgService freeImgService;


    public Long savePost(ShopWriteFormDto shopWriteFormDto, List<MultipartFile> shopImgFileList) throws Exception{
        // 거래글 등록
        Post post = shopWriteFormDto.createPost();

        cardRepository.save(post);

        // 이미지 등록
        for (int i = 0; i < shopImgFileList.size(); i++){
            ShopImg shopImg = new ShopImg();
            shopImg.setPost(post);

            if (i == 0)
                shopImg.setRepImgYn("Y");
            else
                shopImg.setRepImgYn("N");
            shopImgService.saveShopImg(shopImg, shopImgFileList.get(i));
        }
        return post.getId();
    }

    public Long saveFreePost(FreePostFormDto freePostFormDto, List<MultipartFile> freePostImgFileList) throws Exception{
        // 거래글 등록
        FreePost freePost = freePostFormDto.createFreePost();

        freePostRepository.save(freePost);

        // 이미지 등록
        for (int i = 0; i < freePostImgFileList.size(); i++){
            FreeImg freeImg = new FreeImg();
            freeImg.setFreePost(freePost);

            if (i == 0)
                freeImg.setRepImgYn("Y");
            else
                freeImg.setRepImgYn("N");
            freeImgService.saveFreeImg(freeImg, freePostImgFileList.get(i));
        }
        return freePost.getId();
    }

}
