package com.shop.service;


import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.ItemSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);


    //-----------------------------------카드 가져오기---------------------------------------
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true) // 쿼리문 실행 읽기만 한다.
    public Page<Item> getItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getItemPage(itemSearchDto, pageable);
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + id));
        itemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long id){
        // Entity
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(id);
        // DB 에서 데이터를 가지고 옵니다.
        // DTO
        List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 왜 DTO 만들었나요??

        for (ItemImg itemimg : itemImgList){
            // Entity -> DTO
            ItemImgDto itemImgDto = ItemImgDto.of(itemimg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // ITem -> ItemFormDto modelMapper
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        // 상품 변경
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        //상품 이미지 변경
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for (int i = 0; i < itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }


    //-------------------------------------카드 등록 DB저장-------------------------------------------------
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        // 상품등록
        Item item = itemFormDto.createItem();

        itemRepository.save(item);

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    //-------------------------------필터링----------------------------------------------
    public List<Item> getFilteredItems(FilterData filterData) {
        // 필터링된 아이템 조회
        Specification<Item> spec = ItemSpecification.filterItems(filterData);
        List<Item> items = itemRepository.findAll(spec);

        // 디버깅: 아이템 로그 출력
        items.forEach(item -> logger.info("Item: " + item.toString()));

        return items;
    }

    private List<String> parseFilterString(String filterString) {
        if (filterString == null || filterString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(filterString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
