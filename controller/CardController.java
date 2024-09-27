package com.shop.controller;


import com.shop.dto.*;
import com.shop.entity.Deck;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CardController {

    private final ItemService itemService;
    private final DeckService deckService;
    private final CardService cardService;
    private final PostService postService;
    private final FreePostService freePostService;
    private final HttpSession httpSession;
    private final MemberService memberService;
    private final CommentService commentService;

    //------------------------------------------카드보기-----------------------------------

    @GetMapping(value = "/card/info")
    public String allCardInfo (Model model, Principal principal){

        String email = getEmailFromPrincipalOrSession(principal);
        Member member = memberService.findByEmail(email);

        model.addAttribute("member", member);

        return "card/allCardInfo";
    }

    //--------------------------------------카드 상세보기---------------------------------------

    @GetMapping("/cardDetail/{id}")
    public String getCardDetail(@PathVariable("id") Long id, Model model) {
        // 아이템 서비스에서 아이템을 찾습니다.
        ItemFormDto itemFormDto = itemService.getItemDtl(id);

        model.addAttribute("item", itemFormDto);

        return "card/cardDetail";
    }

    //--------------------------------------------게시판-----------------------------------------

    @GetMapping("/card/deckBoard")
    public String cardDeckBoard(Model model, @RequestParam Optional<Integer> page) {

        DeckSearchDto deckSearchDto = new DeckSearchDto();
        int pageNumber = page.orElse(0); // 기본값을 1로 설정
        Pageable pageable = PageRequest.of(pageNumber, 5); // 페이지 번호를 0 기반으로 조정

        // 모든 덱을 페이지 요청으로 가져옵니다.
        Page<Deck> decks = deckService.getAllDecks(deckSearchDto, pageable);

        // decks 객체를 모델에 추가하여 템플릿에서 직접 사용하도록 합니다.
        model.addAttribute("decks", decks);

        return "card/deckBoardMain"; // 뷰 이름 반환
    }


    //----------------------------------------------거래소--------------------------------------

    @GetMapping("/card/shop")
    public String cardShop(Model model, @RequestParam Optional<Integer> page) {

        CardSearchDto cardSearchDto = new CardSearchDto();
        int pageNumber = page.orElse(0);
        Pageable pageable = PageRequest.of(pageNumber, 5);

        Page<ShopWriteFormDto> posts = postService.getCustomerPage(cardSearchDto, pageable);

        model.addAttribute("dtos", posts);

        return "card/cardShop"; // 뷰 이름을 반환합니다.
    }

    //----------------------------------------거래소 글작성 ----------------------------------------------
    @GetMapping(value="/card/shopWrite")
    public String shopWrite(Model model, Principal principal){

        ShopWriteFormDto shopWriteFormDto = new ShopWriteFormDto();
        String email = getEmailFromPrincipalOrSession(principal);
        Member member = memberService.findByEmail(email);
        shopWriteFormDto.setNickName(member.getNickName());

        model.addAttribute("shopWriteFormDto", shopWriteFormDto);
        return "card/cardShopWriteForm";

    }

    @PostMapping(value = "/card/new")
    public String shopNew(@Valid ShopWriteFormDto shopWriteFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("shopImgFile") List<MultipartFile> shopImgFileList, Optional<Integer> page){

        // 입력 값 검증에서 오류가 발생하면 다시 상품 등록 폼을 보여줍니다.
        if (bindingResult.hasErrors()){

            System.out.println("검증오류");
            return "card/cardShopWriteForm";
        }
        try {
            // 상품 정보를 저장합니다.
            cardService.savePost(shopWriteFormDto, shopImgFileList);
            System.out.println("저장완료");
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            System.out.println("알수없는 오류");
            return "card/cardShopWriteForm";
        }

        CardSearchDto cardSearchDto = new CardSearchDto();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<ShopWriteFormDto> posts = postService.getCustomerPage(cardSearchDto,pageable);

        model.addAttribute("dtos", posts);
        model.addAttribute("cardSearchDto", cardSearchDto);
        model.addAttribute("maxPage", 5);

        return "card/cardShop";
    }

    //----------------------------------------거래 글 조회---------------------------------------------

    @GetMapping(value = "card/view/{id}")
    public String viewCustomerCenterPost(@PathVariable Long id, Model model, Principal principal) {

        ShopWriteFormDto shopWriteFormDto = postService.getPostById(id);
        List<ShopCommentDto> comments = commentService.getShopCommentsByPostId(id);
        String email = getEmailFromPrincipalOrSession(principal);
        Member member = memberService.findByEmail(email);

        model.addAttribute("member",member);
        model.addAttribute("comments",comments);
        model.addAttribute("post", shopWriteFormDto);
        //세션을 통해 로그인한사람 확인하고 조회할때 로그인한사람, 게시글정보를 봅니다.
        return "card/postView"; // 상세 정보를 보여줄 뷰 페이지로 이동합니다.
    }

    //----------------------------------------거래 글 삭제----------------------------------------------

    @PostMapping("/card/deletePost")
    public String deletePost(
            @RequestParam("postId") Long postId,
            RedirectAttributes redirectAttributes, Optional<Integer> page, Model model) {

        try {
            // 게시글 삭제
            postService.deletePostById(postId);
            redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        } catch (Exception e) {
            // 삭제 중 오류 발생 시
            redirectAttributes.addFlashAttribute("error", "게시글 삭제에 실패했습니다.");
        }

        CardSearchDto cardSearchDto = new CardSearchDto();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<ShopWriteFormDto> posts = postService.getCustomerPage(cardSearchDto,pageable);
        //받아온 notice를 가지고 set으로 정해주고 그값을 통해서 조회해서 데이터를 빼옵니다
        // 문의사항(user) 공지사항(admin) 정하고 그거에 맞춰서 조회해서 페이지를 만들어서 값을 넣어줍니다.
        // Entity를 DTO로 변환하여 모델에 추가

        model.addAttribute("dtos", posts);
        model.addAttribute("cardSearchDto", cardSearchDto);
        model.addAttribute("maxPage", 5);

        // 게시글 목록 페이지로 리다이렉트
        return "card/cardShop";
    }

    //----------------------------------------자유 게시판-----------------------------------------------

    @GetMapping(value = "/card/board")
    public String board (Model model, Optional<Integer> page){

        FreePostSearchDto freePostSearchDto = new FreePostSearchDto();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainFreePostDto> freeposts = freePostService.getfreePostPage(freePostSearchDto,pageable);
        //받아온 notice를 가지고 set으로 정해주고 그값을 통해서 조회해서 데이터를 빼옵니다
        // 문의사항(user) 공지사항(admin) 정하고 그거에 맞춰서 조회해서 페이지를 만들어서 값을 넣어줍니다.
        // Entity를 DTO로 변환하여 모델에 추가

        model.addAttribute("dtos", freeposts);
        model.addAttribute("freePostSearchDto", freePostSearchDto);
        model.addAttribute("maxPage", 5);

        return "card/freeBoard";
    }

    @GetMapping(value="/card/freePostWrite")
    public String freePostWrite(Model model, Principal principal){

        FreePostFormDto freePostFormDto = new FreePostFormDto();

        String email = getEmailFromPrincipalOrSession(principal);
        Member member = memberService.findByEmail(email);
        freePostFormDto.setNickName(member.getNickName());
        System.out.println(freePostFormDto.getNickName());

        model.addAttribute("freePostFormDto", freePostFormDto);

        return "card/FreePostWriteForm";
    }

    @PostMapping(value = "/freePost/new")
    public String shopNew(@Valid FreePostFormDto freePostFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("freePostImgFile") List<MultipartFile> freePostImgFileList, Optional<Integer> page){

        // 입력 값 검증에서 오류가 발생하면 다시 상품 등록 폼을 보여줍니다.
        if (bindingResult.hasErrors()){

            System.out.println("검증오류");
            return "card/FreePostWriteForm";
        }
        try {
            // 상품 정보를 저장합니다.
            cardService.saveFreePost(freePostFormDto, freePostImgFileList);
            System.out.println("저장완료");
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            System.out.println("알수없는 오류");
            return "card/cardShopWriteForm";
        }

        FreePostSearchDto freePostSearchDto = new FreePostSearchDto();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainFreePostDto> freeposts = freePostService.getfreePostPage(freePostSearchDto,pageable);
        //받아온 notice를 가지고 set으로 정해주고 그값을 통해서 조회해서 데이터를 빼옵니다
        // 문의사항(user) 공지사항(admin) 정하고 그거에 맞춰서 조회해서 페이지를 만들어서 값을 넣어줍니다.
        // Entity를 DTO로 변환하여 모델에 추가

        model.addAttribute("dtos", freeposts);
        model.addAttribute("freePostSearchDto", freePostSearchDto);
        model.addAttribute("maxPage", 5);

        return "card/freeBoard";
    }

    @GetMapping(value = "/freePost/view/{id}")
    public String viewFreePostPost(@PathVariable Long id, Model model,Principal principal) {
        // id를 이용하여 글 상세 정보를 조회합니다.

        FreePostFormDto freePostFormDto = freePostService.getFreePostById(id);
        List<CommentDto> comments = commentService.getCommentsByPostId(id);
        String email = getEmailFromPrincipalOrSession(principal);
        Member member = memberService.findByEmail(email);

        // 모델에 글 정보를 추가합니다.
        model.addAttribute("post", freePostFormDto);
        model.addAttribute("member", member);
        model.addAttribute("comments", comments);
        //세션을 통해 로그인한사람 확인하고 조회할때 로그인한사람, 게시글정보를 봅니다.
        return "card/freePostView"; // 상세 정보를 보여줄 뷰 페이지로 이동합니다.
    }

    //--------------------------------자유게시글 삭제-------------------------------

    @PostMapping("/card/deleteFreePost")
    public String deleteFreePost(
            @RequestParam("postId") Long postId,
            RedirectAttributes redirectAttributes, Optional<Integer> page, Model model) {

        try {
            // 게시글 삭제
            freePostService.deleteFreePostById(postId);
            redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        } catch (Exception e) {
            // 삭제 중 오류 발생 시
            redirectAttributes.addFlashAttribute("error", "게시글 삭제에 실패했습니다.");
        }

        FreePostSearchDto freePostSearchDto = new FreePostSearchDto();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainFreePostDto> freeposts = freePostService.getfreePostPage(freePostSearchDto,pageable);
        //받아온 notice를 가지고 set으로 정해주고 그값을 통해서 조회해서 데이터를 빼옵니다
        // 문의사항(user) 공지사항(admin) 정하고 그거에 맞춰서 조회해서 페이지를 만들어서 값을 넣어줍니다.
        // Entity를 DTO로 변환하여 모델에 추가

        model.addAttribute("dtos", freeposts);
        model.addAttribute("freePostSearchDto", freePostSearchDto);
        model.addAttribute("maxPage", 5);

        // 게시글 목록 페이지로 리다이렉트
        return "card/freeBoard";
    }

    //---------------------------------------------로그인 정보 찾기 ---------------------------------------------

    private String getEmailFromPrincipalOrSession(Principal principal) {
        SessionUser user = (SessionUser) httpSession.getAttribute("member");
        if (user != null) {
            return user.getEmail();
        }
        return principal.getName();
    }
}
