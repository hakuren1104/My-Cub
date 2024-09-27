package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberPasswordDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity // 나 엔티티야
@Table(name = "member") // 테이블 명
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    //기본키 컬럼명 = member_id AI-> 데이터 저장시 1씩 증가
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //알아서 설정
    private String name;
    private String nickName;
    // 중복 허용 X
    @Column(unique = true)
    private String email;
    //알아서
    private String password;
    //알아서
    private Address address; // 주소 정보
    private String provider;
    private String picture;

    private String tel;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member() {
    }

    @Getter
    @Setter
    @Embeddable
    public static class Address{

        private String zipcode;
        private String streetAdr;
        private String detailAdr;

        protected Address(){

        }

        public Address(String zipcode, String streetAdr, String detailAdr){
            this.zipcode = zipcode;
            this.streetAdr = streetAdr;
            this.detailAdr = detailAdr;
        }
    }

    public Member update(String name, String email, String picture, String provider) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
        return this;
    }

    public void updateMemberPassword(MemberPasswordDto memberPasswordDto, PasswordEncoder passwordEncoder){
        String Newpassword = passwordEncoder.encode(memberPasswordDto.getPassword());

        this.password = Newpassword;
    }


    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder){
        Member member = new Member();
        Member.Address address = new Member.Address(memberFormDto.getZipcode(), memberFormDto.getStreetAdr(),
                memberFormDto.getDetailAdr());

        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(address);
        member.setTel(memberFormDto.getTel());
        member.setNickName(memberFormDto.getNickName());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }

    public Member(String name, String email, String picture,Role role,String provider, String tel, Address address) {

        address = new Member.Address("없음","없음","없음");

        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role=role;
        this.provider = provider;
        this.tel = "없음";
        this.address = address;

    }

}
