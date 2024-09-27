package com.shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberTelDto {
    @NotNull(message = "전화번호를 입력해주세요")
    private String Tel;
}