package com.shop.repository;


import com.shop.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Post, Long> {

}
