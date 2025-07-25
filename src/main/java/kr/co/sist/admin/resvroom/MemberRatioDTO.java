package kr.co.sist.admin.resvroom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberRatioDTO {
    private String category; // "회원" 또는 "비회원"
    private int value;
}