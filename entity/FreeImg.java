package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "free_img")
@Getter
@Setter
public class FreeImg {
    @Id
    @Column(name = "free_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_post_id")
    private FreePost freePost;

    public void updateFreePostImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName =oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
