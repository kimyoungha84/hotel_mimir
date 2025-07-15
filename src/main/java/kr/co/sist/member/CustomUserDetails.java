package kr.co.sist.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private int userNum;

    public CustomUserDetails(String email_id, String password, Collection<? extends GrantedAuthority> authorities, int userNum) {
        super(email_id, password, authorities);
        this.userNum = userNum;
    }

    public int getUserNum() {
        return userNum;
    }
}
