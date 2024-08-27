package org.bts.backend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.constant.AuthProvider;
import org.bts.backend.domain.constant.Role;
import org.bts.backend.dto.response.MemberResponse;
import org.bts.backend.exception.after_servlet.NoCredException;
import org.bts.backend.exception.after_servlet.UsernameNotFoundException;
import org.bts.backend.repository.MemberRepository;
import org.bts.backend.util.MailProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final MailProvider mailProvider;

    @Override
    public void saveLocalMember(String name, String email, String password) {
        if (mailProvider.checkAck(email)) {
            List<Role> roles = List.of(Role.ROLE_USER, Role.ROLE_VERIFIED);
            Member member = Member.of(email, name, password, AuthProvider.LOCAL, roles);
            memberRepository.save(member);
        }
        else {
            throw new NoCredException();
        }
    }

    @Override
    public boolean checkMemberEmail(String email) {
        return !memberRepository.existsByEmail(email);
    }

    @Override
    public void sendCertMail(String email) {
        mailProvider.sendMail(email);
    }

    @Override
    public boolean checkCertMail(String email, String uuid) {
        return mailProvider.checkMail(email, uuid);
    }

    @Override
    public List<MemberResponse> findAllMember() {
        return memberRepository.findAll()
                               .stream()
                               .map(MemberResponse::toResponse)
                               .toList();
    }

    // 인증시 사용자를 검색하는 메소드.
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
