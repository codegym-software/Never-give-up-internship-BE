package com.example.demo.security;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;


    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User u = repo.findByUsername(identifier)
                .or(() -> repo.findByEmail(identifier))
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        Role role = u.getRole();
//        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
//        role.getPermissions().forEach(p -> {
//            grantedAuthorities.add(new SimpleGrantedAuthority(p.getName()));
//        });

        return new CustomUserDetails(u);
    }
}
