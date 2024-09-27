package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom,
        CrudRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    List<Item> findByCardColorInAndCardValueInAndCardSeriesIn
            (List<String> colors, List<String> values, List<String> series);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.itemImgs img WHERE img.repImgYn = 'Y'")
    List<Item> findItemsWithImages();
}
