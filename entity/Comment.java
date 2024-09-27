package com.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.dto.CommentDto;
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
@Table(name = "comment")
@NoArgsConstructor
public class Comment {

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
    @JoinColumn(name = "freePost_id")
    @JsonBackReference
    private FreePost freePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public Comment(FreePost freePost, Member member, String comment, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.freePost = freePost;
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
                ", freePostId=" + (freePost != null ? freePost.getId() : "none") +
                '}';
    }

    public CommentDto toDTO() {
        CommentDto dto = new CommentDto();
        dto.setId(this.id);
        dto.setComment(this.comment);
        dto.setMemberId(this.member != null ? this.member.getId() : 999999999 );
        dto.setMemberNickName(this.member != null ? this.member.getNickName() : "Unknown");
        dto.setCreatedDate(this.createdDate);
        return dto;
    }
}