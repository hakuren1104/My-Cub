package com.shop.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.FreePostFormDto;
import com.shop.dto.FreePostSearchDto;
import com.shop.dto.MainFreePostDto;
import com.shop.dto.QMainFreePostDto;
import com.shop.entity.QFreeImg;
import com.shop.entity.QFreePost;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FreePostCustomImpl implements FreePostCustom{

    private final JPAQueryFactory queryFactory;

    public FreePostCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MainFreePostDto> getAllpage(FreePostSearchDto freePostSearchDto, Pageable pageable) {
        QFreePost freePost = QFreePost.freePost;
        QFreeImg freeImg = QFreeImg.freeImg;

        // Querydsl을 사용하여 FreePost와 FreeImg를 조인하고 MainFreePostDto로 변환
        QueryResults<MainFreePostDto> results = queryFactory
                .select(new QMainFreePostDto(
                        freePost.id,
                        freePost.nickName,
                        freePost.title,
                        freePost.content,
                        freeImg.imgUrl)) // 필요한 필드 선택
                .from(freePost)
                .leftJoin(freeImg).on(freeImg.freePost.id.eq(freePost.id).and(freeImg.repImgYn.eq("Y")))
                .orderBy(freePost.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainFreePostDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

}
