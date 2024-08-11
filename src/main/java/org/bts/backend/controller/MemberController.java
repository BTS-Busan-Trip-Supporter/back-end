package org.bts.backend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.request.MailCertRequest;
import org.bts.backend.dto.request.MemberRequest;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.dto.response.MemberResponse;
import org.bts.backend.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// TODO: Swagger를 이용하여 API 문서화.
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register/member/local")
    public ResponseEntity<ApiResponse<String>> registerLocalMember(@RequestBody MemberRequest request) {
        memberService.saveLocalMember(request.name(), request.email(), passwordEncoder.encode(request.password()));
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    // 중복검사
    @GetMapping("/duplicate/email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> checkMemberEmail(@PathVariable String email) {
        boolean result = memberService.checkMemberEmail(email);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    // 메일인증 전송요청
    @GetMapping("/send/mail/{email}")
    public void sendCertMail(@PathVariable String email) {
        memberService.sendCertMail(email);
    }

    // 메일인증 확인
    @PostMapping("/check/mail")
    public ResponseEntity<ApiResponse<Boolean>> checkCertMail(@RequestBody MailCertRequest request) {
        boolean result = memberService.checkCertMail(request.email(), request.uuid());
        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @GetMapping("/members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> searchAllMember() {
        List<MemberResponse> allMember = memberService.findAllMember();
        return ResponseEntity.ok(ApiResponse.success(allMember));
    }

}
