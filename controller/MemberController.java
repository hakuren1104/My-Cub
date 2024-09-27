package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MailService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final HttpSession httpSession;
    private final Map<String, String> phoneVerificationCodes = new HashMap<>();
    private final MemberRepository memberRepository;

    String confirm = "";
    Boolean confirmCheck = true;
    Boolean confirmFindCheck = true;
    Boolean confirmSMSCheck = true;


    //--------------------------------------회원가입--------------------------------------------

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "member/memberForm";
    }


    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        if (!confirmCheck) {
            //메일인증 안할시 모델로 에러메세지 보내서 이메일 인증하라고 보냄
            model.addAttribute("errorMessage", "이메일 인증을 하세요.");
            return "member/memberForm";
        }
        try {

            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "member/memberLoginForm";
    }

    //--------------------------------------로그인--------------------------------------------

    @PostMapping(value = "/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }


    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }

    //--------------------------------------이메일 인증--------------------------------------------

    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfrim(@PathVariable("email") String email)
            throws Exception{
        confirm = mailService.sendSimpleMessage(email);
        return new ResponseEntity<String>("인증 메일을 보냈습니다.", HttpStatus.OK);
    }

    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code")String code)
            throws Exception{
        if(confirm.equals(code)){
            confirmCheck = true;
            return new ResponseEntity<String>("인증 성공하였습니다.",HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증 코드를 올바르게 입력해주세요.",HttpStatus.BAD_REQUEST);
    }

    //---------------------------------------------로그인 정보 찾기 ---------------------------------------------

    private String getEmailFromPrincipalOrSession(Principal principal) {
        SessionUser user = (SessionUser) httpSession.getAttribute("member");
        if (user != null) {
            return user.getEmail();
        }
        return principal.getName();
    }
    //----------------------------------------------SMS 인증 -----------------------------------------------------

    @RequestMapping(value = "/phoneCheck", method = RequestMethod.GET)
    @ResponseBody
    public String sendVerificationCode(@RequestParam String phoneNumber) {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr.append(ran);
        }

        String verificationCode = numStr.toString();

        phoneVerificationCodes.put(phoneNumber, verificationCode);

        // 실제로 SMS를 보내는 부분
        memberService.certifiedPhoneNumber(phoneNumber, verificationCode);

        return "인증번호가 전송되었습니다.";
    }

    @PatchMapping("/verifyCode")
    @ResponseBody
    public ResponseEntity<?> verifyCode(@RequestBody SMSChk smsChk) {
        String storedCode = phoneVerificationCodes.get(smsChk.getPhoneNumber());

        System.out.println("핸드폰 번호"+ smsChk.getPhoneNumber());
        System.out.println("인증해야할 코드"+ storedCode);
        System.out.println("인증코드"+smsChk.getVerificationCode());

        if (storedCode != null && storedCode.equals(smsChk.getVerificationCode())) {
            confirmSMSCheck = true;
            return ResponseEntity.badRequest().body("인증번호 확인 완료");
        } else {
            return ResponseEntity.ok(smsChk.getVerificationCode());
        }
    }

    //-----------------------------------------아이디 찾기------------------------------------------------------

    @GetMapping(value = "/email")
    public String memberFindEmail(Model model){

        MemberTelDto memberTelDto = new MemberTelDto();

        model.addAttribute("memberTelDto",memberTelDto);

        return "member/memberFindEmail";

    }
    @PostMapping(value = "/FindEmail2")
    public String memberFindEmail2(@Valid MemberTelDto memberTelDto, BindingResult bindingResult, Model model){

        /*
        if(!confirmSMSCheck){
            model.addAttribute("errorMessage", "문자 인증을 하세요.");
            return "/member/memberFindEmail";
        }
         */
        if (bindingResult.hasErrors()) {
            MemberTelDto memberTelDto1=new MemberTelDto();
            model.addAttribute("memberTelDto",memberTelDto1);

            return "member/memberFindEmail";
        }

        confirmSMSCheck = false;
        Member member = memberService.findByTel(memberTelDto.getTel());
        if(member == null){
            model.addAttribute("errorMessage", "해당 번호로 가입된 아이디가 없습니다.");
            return "/member/memberFindEmail";
        }
        model.addAttribute("member",member);
        return "member/memberLoginForm2";
    }

    // --------------------------------------------------비밀번호 찾기 ------------------------------------------

    @GetMapping(value = "/findpassword")
    public String memberFindPassword(Model model){

        MemberEmailDto memberEmailDto = new MemberEmailDto();

        model.addAttribute("memberEmailDto",memberEmailDto);

        return "member/memberFindPassword";

    }


    @PostMapping(value = "/FindPassword2")
    public String memberFindPassword2(@Valid MemberEmailDto memberEmailDto, BindingResult bindingResult,
                                      Model model) {

        //유효성 검사
        if (bindingResult.hasErrors()) {

            MemberEmailDto memberEmailDto1=new MemberEmailDto();
            model.addAttribute("memberEmailDto",memberEmailDto1);

            return "member/memberFindPassword";
        }
        //인증이 안되어있으면 다시 돌려보내기
        if (!confirmFindCheck) {
            model.addAttribute("errorMessage", "이메일 인증을 하세요.");
            MemberEmailDto memberEmailDto1=new MemberEmailDto();
            model.addAttribute("memberEmailDto",memberEmailDto1);
            return "member/memberFindPassword";
        }
        confirmFindCheck = false;
        Member member = memberRepository.findByEmail(memberEmailDto.getEmail());
        MemberPasswordDto memberPasswordDto = new MemberPasswordDto();
        memberPasswordDto.setEmail(member.getEmail());

        model.addAttribute("member",member);
        model.addAttribute("memberPasswordDto",memberPasswordDto);

        return "member/memberFindPassword2";
    }

    @PostMapping(value = "/memberUpdatePassword")
    public String memberUpdatePassword1(@Valid MemberPasswordDto memberPasswordDto, BindingResult bindingResult,
                                        Model model) {

        //유효성 검사
        if (bindingResult.hasErrors()) {

            MemberPasswordDto memberPasswordDto1=new MemberPasswordDto();
            model.addAttribute("memberPasswordDto",memberPasswordDto1);

            return "member/memberFindPassword2";
        }

        memberService.updatePassword(memberPasswordDto, passwordEncoder);

        return "member/memberLoginForm";
    }

}
