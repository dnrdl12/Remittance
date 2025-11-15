package io.dnrdl12.remittance.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * packageName    : io.dnrdl12.remittance.dto
 * fileName       : TransferDto
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
public class TransferDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "TransferDto.DepositReq", description = "입금 요청 DTO")
    public static class DepositReq {

        @Schema(description = "입금 계좌 SEQ", example = "1")
        private Long accountSeq;

        @Schema(description = "입금 금액", example = "10000")
        private Long amount;

        @Schema(description = "입금 메모", example = "월급 입금")
        private String description;

        @Schema(description = "클라이언트/채널 구분", example = "월급 입금")
        @Comment("클라이언트/채널 구분 (예: WEB, APP, BATCH 등)")
        @Column(name = "client_id", nullable = false, length = 64)
        @Schema(description = "입금 메모", example = "월급 입금")

        private String clientId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "TransferDto.WithdrawReq", description = "출금 요청 DTO")
    public static class WithdrawReq {

        @Schema(description = "출금 계좌 SEQ", example = "1")
        private Long accountSeq;

        @Schema(description = "출금 금액", example = "5000")
        private Long amount;

        @Schema(description = "출금 메모", example = "생활비")
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "TransferDto.TransferReq", description = "계좌이체 요청 DTO")
    public static class TransferReq {

        @Schema(description = "출금 계좌 SEQ", example = "1")
        private Long fromAccountSeq;

        @Schema(description = "입금 계좌 SEQ", example = "2")
        private Long toAccountSeq;

        @Schema(description = "이체 금액", example = "10000")
        private Long amount;

        @Schema(description = "이체 메모", example = "용돈 이체")
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "TransferDto.SimpleResult", description = "입출금 처리 결과 DTO")
    public static class SimpleResult {

        @Schema(description = "계좌 SEQ")
        private Long accountSeq;

        @Schema(description = "처리 금액")
        private Long amount;

        @Schema(description = "처리 후 잔액")
        private Long balance;

        @Schema(description = "비고/메모")
        private String description;

        @Schema(description = "처리 시각")
        private LocalDateTime transactedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "TransferDto.TransferResult", description = "계좌이체 처리 결과 DTO")
    public static class TransferResult {

        @Schema(description = "출금 계좌 처리 결과")
        private SimpleResult fromAccount;

        @Schema(description = "입금 계좌 처리 결과")
        private SimpleResult toAccount;
    }
}