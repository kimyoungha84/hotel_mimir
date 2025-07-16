package kr.co.sist.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {
    private int userNum;
    private String name;

    public CustomUserDetails(String email_id, String password, Collection<? extends GrantedAuthority> authorities, int userNum, String name) {
        super(email_id, password, authorities);
        
        this.userNum = userNum;
        this.name = name;
    }

    
}
