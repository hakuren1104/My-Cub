package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="shop_img")
@Getter
@Setter
public class ShopImg extends BaseEntity{
    @Id
    @Column(name = "shop_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void updateShopImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName =oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public String getImageUrl() {
        return this.imgUrl;
    }
}
