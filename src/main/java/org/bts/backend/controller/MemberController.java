package org.bts.backend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.request.MemberRequest;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.dto.response.MemberResponse;
import org.bts.backend.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register/member/local")
    public void registerLocalMember(@RequestBody MemberRequest request) {
        memberService.saveLocalMember(request.name(), request.email(), request.password());
    }

    @GetMapping("/members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> searchAllMember() {
        List<MemberResponse> allMember = memberService.findAllMember();
        return ResponseEntity.ok(ApiResponse.success(allMember));
    }

}
