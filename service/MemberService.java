package com.shop.service;

import com.shop.dto.MemberPasswordDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Transactional
@RequiredArgsConstructor // final, @NonNull 변수에 붙으면 자동 주입(Autowired)을 해줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository; //자동 주입됨

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member); // 데이터베이스에 저장을 하라는 명령
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다."); // 예외 발생
        }
        findMember = memberRepository.findByTel(member.getTel());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 전화번호입니다."); // 예외 발생
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        //빌더패턴
        return User.builder().username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    //------------------------------------SMS인증---------------------------------------------
    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {
        String api_key = "xxxxxxxxxx"; // 여기에 실제 API 키를 입력하세요
        String api_secret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; // 여기에 실제 API 시크릿을 입력하세요
        Message coolsms = new Message(api_key, api_secret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);
        params.put("from", "xxxxxxxxx"); // 여기에는 실제 발신 번호를 입력하세요
        params.put("type", "SMS");
        params.put("text", "My Cub 인증번호는 [" + cerNum + "] 입니다.");
        params.put("app_version", "test app 1.2");

        try {
            coolsms.send(params); // 전송 결과를 이용해 다른 작업을 하고 싶다면 여기서 처리
            System.out.println("SMS 전송 성공");

        } catch (CoolsmsException e) {
            System.out.println("SMS 전송 실패: " + e.getMessage());
            System.out.println("에러 코드: " + e.getCode());
        }
    }
    //-------------------------------------아이디 찾기-------------------------------
    public Member findByTel(String Tel) {
        return memberRepository.findByTel(Tel); // 데이터베이스에 저장을 하라는 명령
    }

    public Member findByEmail(String Email){
        return memberRepository.findByEmail(Email);
    }

    //---------------------------------비밀번호 변경-----------------------------------

    public void updatePassword(MemberPasswordDto memberPasswordDto, PasswordEncoder passwordEncoder){

        Member member = memberRepository.findByEmail(memberPasswordDto.getEmail());
        member.updateMemberPassword(memberPasswordDto,passwordEncoder);

    }

}
