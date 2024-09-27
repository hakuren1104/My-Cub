package com.shop.repository;


import com.shop.entity.Comment;
import com.shop.entity.ShopComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCommentRepository extends JpaRepository<ShopComment, Long> {

    List<ShopComment> findByPostId(Long postId);
}
