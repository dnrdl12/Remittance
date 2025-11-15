package io.dnrdl12.remittance.service;

import io.dnrdl12.remittance.dto.TransferDto;

/**
 * packageName    : io.dnrdl12.remittance.service
 * fileName       : TransferService
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
public interface TransferService {

    TransferDto.SimpleResult deposit(TransferDto.DepositReq req);

    TransferDto.SimpleResult withdraw(TransferDto.WithdrawReq req);

    TransferDto.TransferResult transfer(TransferDto.TransferReq req);
}