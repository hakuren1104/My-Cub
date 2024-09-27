package com.shop.repository;


import com.shop.dto.FilterData;
import com.shop.entity.Item;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemSpecification {

    public static Specification<Item> filterItems(FilterData filterData) {
        return (Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 색상 필터 추가
            if (filterData.getColor() != null && !filterData.getColor().isEmpty()) {
                List<String> colors = parseFilterString(filterData.getColor());
                predicates.add(root.get("cardColor").in(colors));
            }

            // 값 필터 추가
            if (filterData.getValue() != null && !filterData.getValue().isEmpty()) {
                List<String> values = parseFilterString(filterData.getValue());
                predicates.add(root.get("cardValue").in(values));
            }

            // 시리즈 필터 추가
            if (filterData.getSeries() != null && !filterData.getSeries().isEmpty()) {
                List<String> series = parseFilterString(filterData.getSeries());
                predicates.add(root.get("cardSeries").in(series));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static List<String> parseFilterString(String filterString) {
        // 빈 문자열 처리
        if (filterString == null || filterString.isEmpty()) {
            return List.of();
        }
        // 쉼표로 구분된 문자열을 리스트로 변환
        return Arrays.stream(filterString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
