package com.shop.repository;


import com.shop.entity.FreePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreePostRepository extends JpaRepository<FreePost, Long> {
    Optional<FreePost> findById(Long id);
}
