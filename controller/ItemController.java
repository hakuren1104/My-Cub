package com.shop.controller;


import com.shop.dto.FilterData;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 새로운 카드 등록 폼을 보여줍니다.
    @GetMapping(value = "/user/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    // 새로운 카드를 등록합니다.
    @PostMapping(value = "/user/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        // 입력 값 검증에서 오류가 발생하면 다시 상품 등록 폼을 보여줍니다.
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        // 첫 번째 상품 이미지는 필수 입력 값입니다.
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            // 상품 정보를 저장합니다.
            itemService.saveItem(itemFormDto, itemImgFileList);
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    //-----------------------------------카드 관리---------------------------------------
    @GetMapping(value = {"/items/manager"})
    public String itemManager(@RequestParam Optional<Integer> page, Model model){

        ItemSearchDto itemSearchDto = new ItemSearchDto();
        int pageNumber = page.orElse(0);
        Pageable pageable = PageRequest.of(pageNumber, 20);

        Page<Item> items = itemService.getItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);

        return "item/itemMng";
    }

    @GetMapping(value = "/items/edit/{id}")
    public String itemDtl(@PathVariable("id") Long id, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(id);
            model.addAttribute("itemFormDto", itemFormDto);
        }
        catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());

            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/user/item/{id}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model){

        // 입력 값 검증에서 오류가 발생하면 다시 상품 수정 폼을 보여줍니다.
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        // 첫 번째 상품 이미지는 필수 입력 값입니다.
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            // 상품 정보를 수정합니다.
            itemService.updateItem(itemFormDto, itemImgFileList);
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/"; // 다시 실행 /
    }

    @PostMapping("/user/item/delete/{id}")
    public String deleteItem(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItem(id);
            redirectAttributes.addFlashAttribute("message", "아이템이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "아이템 삭제에 실패했습니다.");
        }
        return "redirect:/items/manager";
    }




    //------------------------------------필터링------------------------------------------
    @PostMapping("/items/filter")
    public String getFilteredItems(@RequestBody FilterData filterData, Model model) {

        // 필터링된 아이템 리스트를 가져옴
        List<Item> items = itemService.getFilteredItems(filterData);

        items.forEach(item -> System.out.println("Item: " + item.toString()));

        // Thymeleaf 템플릿으로 렌더링하여 HTML 반환
        model.addAttribute("items", items);

        return "fragments/itemList :: itemListFragment"; // 프래그먼트 이름
    }
}
