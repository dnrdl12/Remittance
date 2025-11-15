package io.dnrdl12.remittance.entity;

import io.dnrdl12.remittance.comm.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * packageName    : io.dnrdl12.remittance.entity
 * fileName       : BalanceSnapshot
 * author         : JW.CHOI
 * date           : 2025-11-12
 * description    : 계좌 잔액 스냅샷 (조회 성능용 캐시)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-12        JW.CHOI            최초 생성
 * 2025-11-15        JW.CHOI            컬럼 코멘트 추가
 */
@Entity
@Table(name = "balance_snapshots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class BalanceSnapshot extends BaseEntity {

    @Id
    @Schema(description = "계좌 ID (account.account_seq와 동일, PK이자 FK)")
    @Comment("계좌 ID (account.account_seq와 동일, PK이자 FK)")
    @Column(name = "account_seq")
    private Long accountSeq;

    @Schema(description = "스냅샷 잔액")
    @Comment("스냅샷 잔액 (해당 계좌의 최신 잔액)")
    @Column(name = "balance", nullable = false)
    private Long balance;


    /**
     * Account 연관관계 (1:1)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "account_seq")
    private Account account;
}
