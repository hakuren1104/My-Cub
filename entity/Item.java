package com.shop.entity;


import com.shop.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity //db에 저장
@Table(name="item1") //테이블명 item
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    @Id  //PK
    @Column(name="item_id") //칼럼 이름
    @GeneratedValue(strategy = GenerationType.AUTO) //PK auto로 증가
    private Long id; //

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemImg> itemImgs;

    @Column(nullable = false,length = 50) //not null 길이 50
    private String itemNm; //카드명

    @Lob  //대용량 저장
    @Column(nullable = false) // not null
    private String itemDetail; //상품상세설명

    @Column(nullable = false)
    private String cardColor;

    @Column(nullable = false)
    private String cardValue;

    @Column(nullable = false)
    private String cardSeries;



//    private LocalDateTime regTime; //등록시간

//    private LocalDateTime updateTime; //수정시간

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.itemDetail = itemFormDto.getItemDetail();
    }
}
