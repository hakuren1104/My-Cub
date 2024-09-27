package com.shop.service;


import com.shop.dto.*;
import com.shop.entity.FreePost;
import com.shop.entity.Post;
import com.shop.repository.FreePostCustom;
import com.shop.repository.FreePostRepository;
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
public class FreePostService {

    private final FreePostCustom freePostCustom;
    private final FreePostRepository freePostRepository;

    @Transactional(readOnly = true)
    public Page<MainFreePostDto> getfreePostPage(FreePostSearchDto freePostSearchDto, Pageable pageable){
        return freePostCustom.getAllpage(freePostSearchDto, pageable);
    }

    public FreePostFormDto getFreePostById(Long id) {

        FreePost freePost = freePostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        FreePostFormDto dto = freePost.toDTO();

        // 게시글과 관련된 이미지 URL을 추가
        List<FreeImgDto> freeImgDtoList = freePost.getFreeImgs().stream()
                .map(img -> {
                    FreeImgDto freeImgDto = new FreeImgDto();
                    freeImgDto.setId(img.getId());
                    freeImgDto.setImgName(img.getImgName());
                    freeImgDto.setOriImgName(img.getOriImgName());
                    freeImgDto.setImgUrl(img.getImgUrl());
                    freeImgDto.setRepImgYn(img.getRepImgYn());
                    return freeImgDto;
                })
                .collect(Collectors.toList());
        dto.setFreeImgDtoList(freeImgDtoList);
        return dto;
    }

    public void deleteFreePostById(Long postId) {
        if (freePostRepository.existsById(postId)) {
            freePostRepository.deleteById(postId);
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }


}
