package io.dnrdl12.remittance.controller;

import io.dnrdl12.remittance.dto.TransferDto;
import io.dnrdl12.remittance.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
/**
 * packageName    : io.dnrdl12.remittance.controller
 * fileName       : TransferController
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
@Tag(name = "Transfer API", description = "입금/출금/계좌이체 API")
@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @Operation(summary = "입금 처리", description = "단일 계좌 입금 처리")
    @PostMapping("/deposit")
    public TransferDto.SimpleResult deposit(@RequestBody TransferDto.DepositReq req) {
        return transferService.deposit(req);
    }

    @Operation(summary = "출금 처리", description = "단일 계좌 출금 처리")
    @PostMapping("/withdraw")
    public TransferDto.SimpleResult withdraw(@RequestBody TransferDto.WithdrawReq req) {
        return transferService.withdraw(req);
    }

    @Operation(summary = "계좌 이체 처리", description = "출금 계좌 → 입금 계좌 이체 처리")
    @PostMapping
    public TransferDto.TransferResult transfer(@RequestBody TransferDto.TransferReq req) {
        return transferService.transfer(req);
    }
}