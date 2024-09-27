package com.shop.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.dto.CommentDto;
import com.shop.dto.ShopCommentDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static java.awt.SystemColor.text;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "shopComment")
@NoArgsConstructor
public class ShopComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ShopComment(Post post, Member member, String comment, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.post = post;
        this.member = member;
        this.comment = comment;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.modifiedDate = modifiedDate != null ? modifiedDate : LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", memberId=" + (member != null ? member.getId() : "none") +
                ", postId=" + (post != null ? post.getId() : "none") +
                '}';
    }
    public ShopCommentDto toDTO() {
        ShopCommentDto dto = new ShopCommentDto();
        dto.setId(this.id);
        dto.setComment(this.comment);
        dto.setMemberId(this.member != null ? this.member.getId() : 999999999 );
        dto.setMemberNickName(this.member != null ? this.member.getNickName() : "Unknown");
        dto.setCreatedDate(this.createdDate);
        return dto;
    }

}
