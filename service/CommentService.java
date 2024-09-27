package com.shop.service;

import com.shop.dto.AddCommentRequest;
import com.shop.dto.AddSCRequest;
import com.shop.dto.CommentDto;
import com.shop.dto.ShopCommentDto;
import com.shop.entity.*;
import com.shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FreePostRepository freePostRepository;
    @Autowired
    private ShopCommentRepository shopCommentRepository;


    public CommentDto addComment(AddCommentRequest request) {
        if (request.getFreePostId() == null || request.getMemberId() == null) {
            throw new IllegalArgumentException("Post ID and Member ID must not be null");
        }

        // 유저와 포스트 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        FreePost freePost = freePostRepository.findById(request.getFreePostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 댓글 생성
        Comment comment = Comment.builder()
                .freePost(freePost)
                .member(member)
                .comment(request.getComment())
                .createdDate(LocalDateTime.now()) // 현재 시간 설정
                .modifiedDate(LocalDateTime.now()) // 현재 시간 설정
                .build();

        Comment savedComment = commentRepository.save(comment);

        // CommentDto로 변환하여 반환
        return savedComment.toDTO();
    }

    public ShopCommentDto addShopComment(AddSCRequest request) {
        if (request.getPostId() == null || request.getMemberId() == null) {
            throw new IllegalArgumentException("Post ID and Member ID must not be null");
        }

        // 유저와 포스트 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 댓글 생성
        ShopComment comment = ShopComment.builder()
                .post(post)
                .member(member)
                .comment(request.getComment())
                .createdDate(LocalDateTime.now()) // 현재 시간 설정
                .modifiedDate(LocalDateTime.now()) // 현재 시간 설정
                .build();

        ShopComment savedComment = shopCommentRepository.save(comment);

        // CommentDto로 변환하여 반환
        return savedComment.toDTO();
    }


    public void deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void deleteSComment(Long id) {
        if (shopCommentRepository.existsById(id)) {
            shopCommentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }


    public List<CommentDto> getCommentsByPostId(Long freePostId) {
        List<Comment> comments = commentRepository.findByFreePostId(freePostId);
        return comments.stream()
                .map(Comment::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShopCommentDto> getShopCommentsByPostId(Long postId) {
        List<ShopComment> comments = shopCommentRepository.findByPostId(postId);
        return comments.stream()
                .map(ShopComment::toDTO)
                .collect(Collectors.toList());
    }

}