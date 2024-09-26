package org.bts.backend.service;

import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.constant.Role;
import org.bts.backend.exception.after_servlet.UsernameNotFoundException;
import org.bts.backend.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class SecurityUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(UsernameNotFoundException::new);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : member.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }
}
