package org.bts.backend.service;

import java.util.List;
import org.bts.backend.domain.constant.AuthProvider;
import org.bts.backend.dto.response.MemberResponse;

public interface MemberService {
    void saveLocalMember(String name, String email, String password);

    boolean checkMemberEmail(String email);

    void sendCertMail(String email);

    boolean checkCertMail(String email, String uuid);

//    void saveSocialMember(String name, String email, AuthProvider authProvider);

    // For test
    List<MemberResponse> findAllMember();

}
