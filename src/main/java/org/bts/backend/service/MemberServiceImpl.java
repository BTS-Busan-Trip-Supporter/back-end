package org.bts.backend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.constant.AuthProvider;
import org.bts.backend.dto.response.MemberResponse;
import org.bts.backend.repository.MemberRepository;
import org.bts.backend.util.MailProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final MailProvider mailProvider;

    @Override
    public void saveLocalMember(String name, String email, String password) {
        if (mailProvider.checkAck(email)) {
            Member member = Member.of(email, name, password, AuthProvider.LOCAL);
            memberRepository.save(member);
        }
        else {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }
    }

    @Override
    public boolean checkMemberEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            return false;
        }
        return true;
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
    public void saveSocialMember(String name, String email, AuthProvider authProvider) {
        Member member = Member.of(email, name, null, authProvider);
        memberRepository.save(member);
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
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRoles().stream().map(Enum::name).toList().toString()));

        return org.springframework.security.core.userdetails.User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }
}
