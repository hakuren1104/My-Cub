package com.shop.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.ShopWriteFormDto;
import com.shop.dto.CardSearchDto;
import com.shop.entity.Post;
import com.shop.entity.QPost;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostCustomImpl implements PostCustom {

    private final JPAQueryFactory queryFactory;

    public PostCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ShopWriteFormDto> getAllpage(CardSearchDto cardSearchDto, Pageable pageable) {
        QPost post = QPost.post;

        List<ShopWriteFormDto> content = queryFactory
                .select(Projections.bean(
                        ShopWriteFormDto.class,
                        post.id.as("id"),
                        post.nickName.as("nickName"),
                        post.title.as("title"),
                        post.trade.as("trade"),
                        post.content.as("content")
                        // Additional fields can be mapped here if needed
                ))
                .from(post)
                .where(
                        // Apply filtering conditions based on cardSearchDto
                        // e.g., post.someField.eq(cardSearchDto.getSomeValue())
                )
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .where(
                        // Apply filtering conditions based on cardSearchDto
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
