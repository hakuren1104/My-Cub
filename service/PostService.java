package com.shop.service;


import com.shop.dto.CardSearchDto;
import com.shop.dto.ShopImgDto;
import com.shop.dto.ShopWriteFormDto;
import com.shop.entity.Post;
import com.shop.repository.PostCustom;
import com.shop.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostCustom postCustom;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<ShopWriteFormDto> getCustomerPage(CardSearchDto cardSearchDto, Pageable pageable){
        return postCustom.getAllpage(cardSearchDto, pageable);
    }

    public ShopWriteFormDto getPostById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        ShopWriteFormDto dto = post.toDTO();

        // 게시글과 관련된 이미지 URL을 추가
        List<ShopImgDto> shopImgDtoList = post.getShopImgs().stream()
                .map(img -> {
                    ShopImgDto imgDto = new ShopImgDto();
                    imgDto.setId(img.getId());
                    imgDto.setImgName(img.getImgName());
                    imgDto.setOriImgName(img.getOriImgName());
                    imgDto.setImgUrl(img.getImgUrl());
                    imgDto.setRepImgYn(img.getRepImgYn());
                    return imgDto;
                })
                .collect(Collectors.toList());

        dto.setShopImgDtoList(shopImgDtoList);
        return dto;
    }

    public void deletePostById(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }
}
