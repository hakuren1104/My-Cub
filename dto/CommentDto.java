package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String comment;
    private String memberNickName; // 작성자 닉네임
    private Long memberId;
    private LocalDateTime createdDate;

    // 기본 생성자
    public CommentDto() {}

    // 모든 필드를 받는 생성자
    public CommentDto(Long id, String comment, String memberNickName, Long memberId, LocalDateTime createdDate) {
        this.id = id;
        this.comment = comment;
        this.memberNickName = memberNickName;
        this.memberId = memberId;
        this.createdDate = createdDate;
    }

}
