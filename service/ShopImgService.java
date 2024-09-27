package com.shop.service;


import com.shop.entity.ItemImg;
import com.shop.entity.ShopImg;
import com.shop.repository.ShopImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopImgService {

    @Value("${shopImgLocation}") //application.properties 에 itemImgLocation
    private String shopImgLocation;

    private final ShopImgRepository shopImgRepository;
    private final FileService fileService;

    public void saveShopImg(ShopImg shopImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename(); // 오리지날 이미지 경로
        String imgName = "";
        String imgUrl = "";
        System.out.println(oriImgName);
        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){ // oriImgName 문자열로 비어있지 않으면 실행
            System.out.println("******");
            imgName = fileService.uploadFile(shopImgLocation, oriImgName, itemImgFile.getBytes());
            System.out.println(imgName);
            imgUrl = "/images/trade/" + imgName;
        }
        System.out.println("1111");
        // 상품 이미지 정보 저장
        // oriImgName : 상품 이미지 파일의 원래 이름
        // imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름
        // imgUrl : 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        shopImg.updateShopImg(oriImgName, imgName, imgUrl);
        System.out.println("(((((");
        shopImgRepository.save(shopImg);
    }
}
