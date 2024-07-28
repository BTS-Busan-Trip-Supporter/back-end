package org.bts.backend.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.constant.AuthProvider;
import org.bts.backend.dto.response.MemberResponse;
import org.bts.backend.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void saveLocalMember(String name, String email, String password) {
        Member member = Member.of(email, name, password, AuthProvider.LOCAL);
        memberRepository.save(member);
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
}
