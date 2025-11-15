package io.dnrdl12.remittance.service;

import io.dnrdl12.remittance.comm.api.PagingProperties;
import io.dnrdl12.remittance.comm.enums.AccountStatus;
import io.dnrdl12.remittance.dto.AccountDto;
import io.dnrdl12.remittance.entity.Account;
import io.dnrdl12.remittance.entity.BalanceSnapshot;
import io.dnrdl12.remittance.entity.FeePolicy;
import io.dnrdl12.remittance.entity.Member;
import io.dnrdl12.remittance.repository.AccountRepository;
import io.dnrdl12.remittance.repository.BalanceSnapshotRepository;
import io.dnrdl12.remittance.repository.FeePolicyRepository;
import io.dnrdl12.remittance.repository.MemberRepository;
import io.dnrdl12.remittance.spec.AccountSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * packageName    : io.dnrdl12.remittance.service
 * fileName       : AccountServiceImpl
 * author         : JW.CHOI
 * date           : 2025-11-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-14        JW.CHOI              최초 생성
 */

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final BalanceSnapshotRepository balanceSnapshotRepository;
    private final FeePolicyRepository feePolicyRepository;
    private final PagingProperties pagingProperties;

    @Override
    public AccountDto.Res create(AccountDto.CreateReq req, String userId) {

        if (!memberRepository.existsById(req.getMemberSeq())) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        Member memberRef = memberRepository.getReferenceById(req.getMemberSeq());
        FeePolicy feePolicyRef = feePolicyRepository.getReferenceById(req.getFeePolicySeq());

        Account account = Account.builder()
                .member(memberRef)
                .accountNumber(generateAccountNumber())
                .nickname(req.getNickname())
                .accountType(req.getAccountType())
                .bankCode(req.getBankCode())
                .branchCode(req.getBranchCode())
                .feePolicy(feePolicyRef)
                .dailyTransferLimit(req.getDailyTransferLimit())
                .dailyWithdrawLimit(req.getDailyWithdrawLimit())
                .build();

        account.setRegId(userId);
        account.setModId(userId);

        Account saved = accountRepository.save(account);
        // 잔액 BalanceSnapshot ?
        return toRes(saved);
    }

    @Override
    public Page<AccountDto.SearchSimpleRes> searchAccounts(AccountDto.SearchReq req) {
        Pageable pageable = PageRequest.of(
                req.getPage() != null ? req.getPage() : 0,
                req.getSize() != null ? Math.min(req.getSize(), pagingProperties.maxSize()) : pagingProperties.defaultSize(),
                Sort.by(Sort.Direction.DESC, "accountSeq")
        );
        Page<Account> page = accountRepository.findAll(AccountSpec.search(req), pageable);
        return page.map(this::toSearchSimpleRes);
    }

    @Override
    public AccountDto.SearchDetailRes getAccountDetail(Long accountSeq) {
        Account account = accountRepository.findByAccountSeq(accountSeq)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다. accountSeq=" + accountSeq));
        return toSearchDetailRes(account);
    }

    private AccountDto.SearchSimpleRes toSearchSimpleRes(Account account) {
        Member m = account.getMember();

        return AccountDto.SearchSimpleRes.builder()
                .accountSeq(account.getAccountSeq())
                .accountNumber(account.getAccountNumber())
                .nickname(account.getNickname())
                .accountStatus(account.getAccountStatus())
                .accountType(account.getAccountType())
                .bankCode(account.getBankCode())
                .branchCode(account.getBranchCode())
                .memberNm(m != null ? m.getMemberNm() : null)
                .memberPhone(m != null ? m.getMemberPhone() : null)
                .memberStatus(m != null ? m.getMemberStatus() : null)
                .privConsentYn(m != null ? m.getPrivConsentYn() : null)
                .msgConsentYn(m != null ? m.getMsgConsentYn() : null)
                .build();
    }

    private AccountDto.SearchDetailRes toSearchDetailRes(Account account) {
        Member m = account.getMember();
        BalanceSnapshot b = account.getBalanceSnapshot();
        FeePolicy f = account.getFeePolicy();

        return AccountDto.SearchDetailRes.builder()
                .accountSeq(account.getAccountSeq())
                .accountNumber(account.getAccountNumber())
                .nickname(account.getNickname())
                .accountStatus(account.getAccountStatus())
                .accountType(account.getAccountType())
                .bankCode(account.getBankCode())
                .branchCode(account.getBranchCode())
                .createdDate(account.getCreatedDate())
                .memberNm(m != null ? m.getMemberNm() : null)
                .memberPhone(m != null ? m.getMemberPhone() : null)
                .memberStatus(m != null ? m.getMemberStatus() : null)
                .privConsentYn(m != null ? m.getPrivConsentYn() : null)
                .msgConsentYn(m != null ? m.getMsgConsentYn() : null)
                .balance(b != null ? b.getBalance() : null)
                .policyName(f != null ? f.getPolicyName() : null)
                .transferFeeRate(f != null ? f.getTransferFeeRate() : null)
                .withdrawFeeRate(f != null ? f.getWithdrawFeeRate() : null)
                .build();
    }


    @Override
    public AccountDto.Res patch(Long accountSeq, AccountDto.PatchReq req, String userId) {
        Account account = accountRepository.findById(accountSeq)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        if (req.getNickname() != null) {
            account.setNickname(req.getNickname());
        }
        if (req.getAccountStatus() != null) {
            account.setAccountStatus(req.getAccountStatus());
        }
        account.setModId(userId);
        return toRes(account);
    }

    @Override
    @Transactional
    public AccountDto.IdResponse deleteSoft(Long accountSeq, String userId) {
        Account account = accountRepository.findById(accountSeq)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다. accountSeq=" + accountSeq));

        account.setAccountStatus(AccountStatus.CLOSED.getCode()); // 해지
        account.setClosedDate(LocalDateTime.now());
        account.setModId(userId);

        return AccountDto.IdResponse.of(account.getAccountSeq());
    }


    private AccountDto.Res toRes(Account a) {
        Long balance = balanceSnapshotRepository.findById(a.getAccountSeq())
                .map(BalanceSnapshot::getBalance)
                .orElse(0L);

        return AccountDto.Res.builder()
                .accountSeq(a.getAccountSeq())
                .accountNumber(a.getAccountNumber())
                .memberSeq(a.getMember().getMemberSeq())
                .nickname(a.getNickname())
                .balance(balance)
                .accountStatus(a.getAccountStatus())
                .createdDate(a.getCreatedDate())
                .build();
    }


    /**
     * 계좌번호 생성 로직 (예시용)
     */
    private String generateAccountNumber() {
        long num = (long) (Math.random() * 1_0000_0000_0000L) + 1_0000_0000_0000L;
        return String.valueOf(num);
    }
}