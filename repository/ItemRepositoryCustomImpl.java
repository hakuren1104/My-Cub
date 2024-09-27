package com.shop.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory; // 동적 쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    // 생성자
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체가 만들어 집니다.
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        // select i.id, id.itemNm, i.itemDetail, im.itemImg, i.price from item i, itemImg im join i.id = im.itemId
        // where im.repImgYn = "Y" and i.itemNm like %searchQuery% order by i.id desc
        // QMainItemDto @QueryProjection 을 허용하면 DTO 로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm,
                        item.cardColor, item.cardValue, item.cardSeries,
                        item.itemDetail, itemImg.imgUrl))
                // join 내부조인 .repImgYn.eq("Y") 대표이미지만 가져온다.
                .from(itemImg).join(itemImg.item).where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Item> getItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        // QueryDSL을 사용하여 쿼리 생성
        QItem qItem = QItem.item;

        // 검색 조건에 맞는 쿼리 작성
        QueryResults<Item> results = queryFactory
                .selectFrom(qItem)
                .orderBy(qItem.id.desc()) // ID 기준으로 내림차순 정렬
                .offset(pageable.getOffset()) // 페이징 오프셋
                .limit(pageable.getPageSize()) // 페이징 크기
                .fetchResults();

        List<Item> content = results.getResults(); // 조회된 결과 리스트
        long total = results.getTotal(); // 전체 결과 수

        return new PageImpl<>(content, pageable, total); // Page 객체 생성 및 반환
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }
}


