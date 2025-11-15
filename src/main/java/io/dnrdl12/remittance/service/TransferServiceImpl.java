package io.dnrdl12.remittance.service;

import io.dnrdl12.remittance.dto.TransferDto;
import io.dnrdl12.remittance.entity.Account;
import io.dnrdl12.remittance.entity.BalanceSnapshot;
import io.dnrdl12.remittance.repository.AccountRepository;
import io.dnrdl12.remittance.repository.BalanceSnapshotRepository;
import io.dnrdl12.remittance.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
/**
 * packageName    : io.dnrdl12.remittance.service
 * fileName       : TransferServiceImpl
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final BalanceSnapshotRepository balanceSnapshotRepository;

    @Override
    @Transactional
    public TransferDto.SimpleResult deposit(TransferDto.DepositReq req) {

        if (req.getAmount() == null || req.getAmount() <= 0L) {
            throw new IllegalArgumentException("입금 금액은 0보다 커야 합니다.");
        }

        Account account = accountRepository.findById(req.getAccountSeq())
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다. accountSeq=" + req.getAccountSeq()));

        // 잔액 스냅샷 조회 + 잠금
        BalanceSnapshot snapshot = balanceSnapshotRepository
                .findByAccountSeqForUpdate(account.getAccountSeq())
                .orElseGet(() -> createNewSnapshot(account));

        long before = snapshot.getBalance() == null ? 0L : snapshot.getBalance();
        long after = before + req.getAmount();

        snapshot.setBalance(after);
        // BaseEntity 에 reg/mod 가 있을테니 따로 시간 필드는 없어도 됨
        balanceSnapshotRepository.save(snapshot);

        LocalDateTime now = LocalDateTime.now();

        return TransferDto.SimpleResult.builder()
                .accountSeq(account.getAccountSeq())
                .amount(req.getAmount())
                .balance(after)
                .description(req.getDescription())
                .transactedAt(now)
                .build();
    }

    @Override
    @Transactional
    public TransferDto.SimpleResult withdraw(TransferDto.WithdrawReq req) {

        if (req.getAmount() == null || req.getAmount() <= 0L) {
            throw new IllegalArgumentException("출금 금액은 0보다 커야 합니다.");
        }

        Account account = accountRepository.findById(req.getAccountSeq())
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다. accountSeq=" + req.getAccountSeq()));

        BalanceSnapshot snapshot = balanceSnapshotRepository
                .findByAccountSeqForUpdate(account.getAccountSeq())
                .orElseGet(() -> createNewSnapshot(account));

        long before = snapshot.getBalance() == null ? 0L : snapshot.getBalance();

        if (before < req.getAmount()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        long after = before - req.getAmount();
        snapshot.setBalance(after);
        balanceSnapshotRepository.save(snapshot);

        LocalDateTime now = LocalDateTime.now();

        return TransferDto.SimpleResult.builder()
                .accountSeq(account.getAccountSeq())
                .amount(req.getAmount())
                .balance(after)
                .description(req.getDescription())
                .transactedAt(now)
                .build();
    }

    @Override
    @Transactional
    public TransferDto.TransferResult transfer(TransferDto.TransferReq req) {

        if (req.getAmount() == null || req.getAmount() <= 0L) {
            throw new IllegalArgumentException("이체 금액은 0보다 커야 합니다.");
        }

        if (req.getFromAccountSeq().equals(req.getToAccountSeq())) {
            throw new IllegalArgumentException("출금 계좌와 입금 계좌가 동일할 수 없습니다.");
        }

        Account from = accountRepository.findById(req.getFromAccountSeq())
                .orElseThrow(() -> new IllegalArgumentException("출금 계좌를 찾을 수 없습니다. accountSeq=" + req.getFromAccountSeq()));

        Account to = accountRepository.findById(req.getToAccountSeq())
                .orElseThrow(() -> new IllegalArgumentException("입금 계좌를 찾을 수 없습니다. accountSeq=" + req.getToAccountSeq()));

        // 데드락 방지를 위해 accountSeq 기준으로 락 순서 고정
        Account first = from.getAccountSeq() < to.getAccountSeq() ? from : to;
        Account second = (first == from) ? to : from;

        BalanceSnapshot firstSnapshot = balanceSnapshotRepository
                .findByAccountSeqForUpdate(first.getAccountSeq())
                .orElseGet(() -> createNewSnapshot(first));

        BalanceSnapshot secondSnapshot = balanceSnapshotRepository
                .findByAccountSeqForUpdate(second.getAccountSeq())
                .orElseGet(() -> createNewSnapshot(second));

        // 다시 출금/입금 대상 매핑
        BalanceSnapshot fromSnapshot = (from == first) ? firstSnapshot : secondSnapshot;
        BalanceSnapshot toSnapshot = (to == first) ? firstSnapshot : secondSnapshot;

        long fromBefore = fromSnapshot.getBalance() == null ? 0L : fromSnapshot.getBalance();

        if (fromBefore < req.getAmount()) {
            throw new IllegalArgumentException("출금 계좌의 잔액이 부족합니다.");
        }

        long fromAfter = fromBefore - req.getAmount();
        long toBefore = toSnapshot.getBalance() == null ? 0L : toSnapshot.getBalance();
        long toAfter = toBefore + req.getAmount();

        fromSnapshot.setBalance(fromAfter);
        toSnapshot.setBalance(toAfter);

        balanceSnapshotRepository.save(fromSnapshot);
        balanceSnapshotRepository.save(toSnapshot);

        LocalDateTime now = LocalDateTime.now();

        TransferDto.SimpleResult fromResult = TransferDto.SimpleResult.builder()
                .accountSeq(from.getAccountSeq())
                .amount(req.getAmount())
                .balance(fromAfter)
                .description(req.getDescription())
                .transactedAt(now)
                .build();

        TransferDto.SimpleResult toResult = TransferDto.SimpleResult.builder()
                .accountSeq(to.getAccountSeq())
                .amount(req.getAmount())
                .balance(toAfter)
                .description(req.getDescription())
                .transactedAt(now)
                .build();

        return TransferDto.TransferResult.builder()
                .fromAccount(fromResult)
                .toAccount(toResult)
                .build();
    }

    private BalanceSnapshot createNewSnapshot(Account account) {
        // 새 계좌의 첫 스냅샷 생성 (잔액 0원)
        return BalanceSnapshot.builder()
                .account(account)
                .balance(0L)
                .build();
    }
}