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
import org.bts.backend.util.JwtTokenProvider;
import org.bts.backend.util.MailProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final MailProvider mailProvider;
    private final JwtTokenProvider tokenUtil;
    private final PasswordEncoder passwordEncoder;

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
    public void changePassword(String accesstoken, String password, String newPassword) {
        Member member = memberRepository.findByEmail(tokenUtil.getEmailForAccessToken(accesstoken))
                                        .orElseThrow(UsernameNotFoundException::new);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UsernameNotFoundException("비밀번호가 일치하지 않습니다.");
        }
        member.changePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

    @Override
    public void changeName(String accesstoken, String name) {
        Member member = memberRepository.findByEmail(tokenUtil.getEmailForAccessToken(accesstoken))
                                        .orElseThrow(UsernameNotFoundException::new);
        member.changeName(name);
        memberRepository.save(member);
    }

    @Override
    public MemberResponse findMember(String accessToken) {
        return memberRepository.findByEmail(tokenUtil.getEmailForAccessToken(accessToken))
                               .map(MemberResponse::toResponse)
                               .orElseThrow(UsernameNotFoundException::new);
    }

    @Override
    public List<MemberResponse> findAllMember() {
        return memberRepository.findAll()
                               .stream()
                               .map(MemberResponse::toResponse)
                               .toList();
    }
}
