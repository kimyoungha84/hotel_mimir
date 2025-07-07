package kr.co.sist.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SelectOption {
    private String value; // → DTO 필드명 (예: title, regDate)
    private String label; // → 화면 표시 텍스트 (예: 제목, 등록일)
}