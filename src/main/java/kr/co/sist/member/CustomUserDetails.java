package kr.co.sist.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private int userNum;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, int userNum) {
        super(username, password, authorities);
        this.userNum = userNum;
    }

    public int getUserNum() {
        return userNum;
    }
}
