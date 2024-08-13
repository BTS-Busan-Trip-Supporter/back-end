package org.bts.backend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.request.MailCertRequest;
import org.bts.backend.dto.request.MemberRequest;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.dto.response.JwtTokenResponse;
import org.bts.backend.dto.response.MemberResponse;
import org.bts.backend.service.MemberService;
import org.bts.backend.service.TokenService;
import org.bts.backend.util.CookieProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// TODO: Swagger를 이용하여 API 문서화.
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final CookieProvider cookieProvider;
    private final TokenService tokenService;

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

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String accessToken){
        // 레디스에 있는 RefreshToken 삭제
        tokenService.logout(accessToken);

        // 쿠키 삭제
        ResponseCookie responseCookie = cookieProvider.deleteRefreshCookie();

        // 헤더에 넣으며 쿠키 업데이트
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.success("로그아웃 되었습니다."));

    }

    @GetMapping("/reissue")
    public ResponseEntity<ApiResponse<?>> reissue(@RequestHeader("Authorization") String accessToken,
                                                        @CookieValue("RefreshToken") String refreshToken){
        // RefreshToken으로 AccessToken 재발급
        JwtTokenResponse jwtTokenResponse = tokenService.updateAccessToken(accessToken, refreshToken);

        // 쿠키 업데이트
        ResponseCookie responseCookie = cookieProvider.createRefreshCookie(jwtTokenResponse.refreshToken());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.success(jwtTokenResponse));
    }

}
