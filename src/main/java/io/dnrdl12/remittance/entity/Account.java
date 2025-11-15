package io.dnrdl12.remittance.entity;

import io.dnrdl12.remittance.comm.constants.AccountConstants;
import io.dnrdl12.remittance.comm.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * packageName    : io.dnrdl12.remittance.entity
 * fileName       : Account
 * author         : JW.CHOI
 * date           : 2025-11-11
 * description    : 계좌 엔티티 (BaseEntity 상속)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-11        JW.CHOI            최초 생성
 * 2025-11-15        JW.CHOI            컬럼 코멘트 추가, 커스텀 빌더 추가, 조인관련 수정
 */
@Entity
@Table(
        name = "account",
        indexes = {
                @Index(name = "ix_account_member", columnList = "member_seq")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "계좌 고유 식별자")
    @Comment("계좌 고유 식별자")
    @Column(name = "account_seq")
    private Long accountSeq;

    @Schema(description = "계좌번호")
    @Comment("계좌번호")
    @Column(name = "account_number", nullable = false, unique = true, length = 30)
    private String accountNumber;

    /**
     * 회원 (FK → Member)
     */
    @Schema(description = "회원 엔티티")
    @Comment("회원 고유 식별자 (FK)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @Schema(description = "계좌 상태: 1 정상, 2 정지, 3 해지")
    @Comment("계좌 상태: 1 정상, 2 정지, 3 해지")
    @Column(name = "account_status", nullable = false)
    @Check(constraints = "account_status IN (1, 2, 3)")
    @Builder.Default
    private Integer accountStatus = AccountConstants.DEFAULT_ACCOUNT_STATUS;

    @Schema(description = "계좌 별칭")
    @Comment("계좌 별칭")
    @Column(name = "nickname", length = 50)
    private String nickname;

    @Schema(description = "계좌 종류: 1 일반, 2 월급통장, 3 한도통장")
    @Comment("계좌 종류: 1 일반, 2 월급통장, 3 한도통장")
    @Column(name = "account_type")
    @Builder.Default
    private Integer accountType = AccountConstants.DEFAULT_ACCOUNT_TYPE;

    @Schema(description = "은행 코드")
    @Comment("은행 코드")
    @Column(name = "bank_code", length = 10)
    @Builder.Default
    private String bankCode = AccountConstants.DEFAULT_BANK_CODE;

    @Schema(description = "지점 코드")
    @Comment("지점 코드")
    @Column(name = "branch_code", length = 10)
    @Builder.Default
    private String branchCode = AccountConstants.DEFAULT_BRANCH_CODE;

    @Schema(description = "계좌 개설일시")
    @Comment("계좌 개설일시")
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Schema(description = "계좌 해지일시")
    @Comment("계좌 해지일시")
    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @Schema(description = "계좌 순번")
    @Comment("계좌 순번 (사용자 계좌 목록 정렬용)")
    @Column(name = "account_sort")
    private Integer accountSort;

    /**
     * FeePolicy 연관관계
     */
    @Schema(description = "수수료 정책 엔티티")
    @Comment("수수료 정책 (FK)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_policy_seq")
    private FeePolicy feePolicy;

    /**
     * BalanceSnapshot 연관관계 (1:1)
     */
    @Schema(description = "잔액 스냅샷 정보")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_seq", referencedColumnName = "account_seq", insertable = false, updatable = false)
    private BalanceSnapshot balanceSnapshot;

    @Schema(description = "1일 이체 한도")
    @Comment("1일 이체 한도 (기본 3,000,000원)")
    @Column(name = "daily_transfer_limit", nullable = false)
    @Builder.Default
    private Long dailyTransferLimit = AccountConstants.DEFAULT_DAILY_TRANSFER_LIMIT;

    @Schema(description = "1일 출금 한도")
    @Comment("1일 출금 한도 (기본 1,000,000원)")
    @Column(name = "daily_withdraw_limit", nullable = false)
    @Builder.Default
    private Long dailyWithdrawLimit = AccountConstants.DEFAULT_DAILY_WITHDRAW_LIMIT;

    @PrePersist
    public void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }

    /**
     * 자동 널처리를 위한 빌더
     */
    public static class AccountBuilder {

        private Integer accountStatus;
        private Integer accountType;
        private String bankCode;
        private String branchCode;
        private Long dailyTransferLimit;
        private Long dailyWithdrawLimit;

        public AccountBuilder accountStatus(Integer val) {
            this.accountStatus = (val == null ? AccountConstants.DEFAULT_ACCOUNT_STATUS : val);
            return this;
        }

        public AccountBuilder accountType(Integer val) {
            this.accountType = (val == null ? AccountConstants.DEFAULT_ACCOUNT_TYPE : val);
            return this;
        }

        public AccountBuilder bankCode(String val) {
            this.bankCode = (val == null ? AccountConstants.DEFAULT_BANK_CODE : val);
            return this;
        }

        public AccountBuilder branchCode(String val) {
            this.branchCode = (val == null ? AccountConstants.DEFAULT_BRANCH_CODE : val);
            return this;
        }

        public AccountBuilder dailyTransferLimit(Long val) {
            this.dailyTransferLimit =
                    (val == null ? AccountConstants.DEFAULT_DAILY_TRANSFER_LIMIT : val);
            return this;
        }

        public AccountBuilder dailyWithdrawLimit(Long val) {
            this.dailyWithdrawLimit =
                    (val == null ? AccountConstants.DEFAULT_DAILY_WITHDRAW_LIMIT : val);
            return this;
        }
    }
}