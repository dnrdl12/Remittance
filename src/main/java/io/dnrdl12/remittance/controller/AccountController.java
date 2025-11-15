package io.dnrdl12.remittance.controller;

import io.dnrdl12.remittance.dto.AccountDto;
import io.dnrdl12.remittance.comm.api.BaseResponse;
import io.dnrdl12.remittance.comm.api.PageDto;
import io.dnrdl12.remittance.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : io.dnrdl12.remittance.controller
 * fileName       : AccountController
 * author         : JW.CHOI
 * date           : 2025-11-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-11        JW.CHOI              최초 생성
 */
@Tag(name = "Account API", description = "계좌 생성/조회/수정/해지 API")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "계좌 생성")
    @PostMapping
    public BaseResponse<AccountDto.Res> create(
            @RequestBody AccountDto.CreateReq req,
            @Parameter(description = "삭제자 ID", example = "admin") @RequestHeader("X-USER-ID") String userId
    ) {
        return BaseResponse.ok(accountService.create(req, userId));
    }

    @Operation(summary = "계좌 정보 수정 (별칭/상태)")
    @PatchMapping("/{seq}")
    public BaseResponse<AccountDto.Res> patch(
            @PathVariable("seq") Long seq,
            @Valid @RequestBody AccountDto.PatchReq req,
            @Parameter(description = "삭제자 ID", example = "admin") @RequestHeader("X-USER-ID") String userId
    ) {
        return BaseResponse.ok(accountService.patch(seq, req, userId));
    }

    @Operation(summary = "계좌 상세 조회", description = "계좌SEQ로 상세 조회")
    @GetMapping("/{accountSeq}")
    public BaseResponse<AccountDto.SearchDetailRes> getAccountDetail( @PathVariable Long accountSeq) {
        return BaseResponse.ok(accountService.getAccountDetail(accountSeq));
    }
    @Operation(summary = "계좌 목록 조회(검색)", description = "회원명, 회원번호, 계좌번호, 계좌SEQ로 가변 검색")
    @GetMapping
    public BaseResponse<Page<AccountDto.SearchSimpleRes>> searchAccounts(AccountDto.SearchReq req) {
        var res = accountService.searchAccounts(req);
        return BaseResponse.ok(res);
    }

    @Operation(summary = "계좌 해지")
    @DeleteMapping("/{seq}")
    public BaseResponse<AccountDto.IdResponse> delete(
            @PathVariable("accountSeq") Long accountSeq,
            @Parameter(description = "삭제자 ID", example = "admin") @RequestHeader("X-USER-ID") String userId
    ) {
        var idRes  = accountService.deleteSoft(accountSeq, userId);
        return BaseResponse.ok(idRes);
    }
}