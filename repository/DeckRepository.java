package com.shop.repository;


import com.shop.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    @Query("SELECT d FROM Deck d LEFT JOIN FETCH d.cards WHERE d.id = :id")
    Optional<Deck> findByIdWithCards(@Param("id") Long id);

}
