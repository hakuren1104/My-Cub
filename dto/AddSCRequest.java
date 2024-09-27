package com.shop.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AddSCRequest {

    private Long postId; // 게시글 ID
    private Long memberId; // 회원 ID
    private String memberNickName; // 닉네임
    private String comment; // 댓글 내용

    // 기본 생성자와 모든 필드를 받는 생성자 생성
    public AddSCRequest(Long postId, Long memberId, String memberNickName, String comment) {
        this.postId = postId;
        this.memberId = memberId;
        this.memberNickName = memberNickName;
        this.comment = comment;
    }
}
