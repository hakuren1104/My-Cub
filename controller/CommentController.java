package com.shop.controller;

import com.shop.dto.AddCommentRequest;
import com.shop.dto.AddSCRequest;
import com.shop.dto.CommentDto;
import com.shop.dto.ShopCommentDto;
import com.shop.entity.Comment;
import com.shop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody AddCommentRequest request) {
        try {
            // 서비스 메서드를 호출하여 CommentDto를 반환받음
            CommentDto commentDto = commentService.addComment(request);
            return ResponseEntity.ok(commentDto);
        } catch (Exception e) {
            // 예외를 로그에 기록합니다.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 추가 중 오류 발생");
        }
    }

    // --------------------------------------댓글 삭제-------------------------------------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok().body("Comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteSC/{id}")
    public ResponseEntity<?> deleteSComment(@PathVariable("id") Long id) {
        try {
            commentService.deleteSComment(id);
            return ResponseEntity.ok().body("Comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting comment: " + e.getMessage());
        }
    }

    //--------------------------------------거래소 댓글 ----------------------------------------------------

    @PostMapping("/addSC")
    public ResponseEntity<?> addShopComment(@RequestBody AddSCRequest request) {
        try {
            // 서비스 메서드를 호출하여 CommentDto를 반환받음
            ShopCommentDto commentDto = commentService.addShopComment(request);
            return ResponseEntity.ok(commentDto);
        } catch (Exception e) {
            // 예외를 로그에 기록합니다.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 추가 중 오류 발생");
        }
    }

}
