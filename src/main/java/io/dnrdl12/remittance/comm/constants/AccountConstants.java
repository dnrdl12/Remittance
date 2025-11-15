package io.dnrdl12.remittance.comm.constants;

/**
 * packageName    : io.dnrdl12.remittance.comm.constants
 * fileName       : AccountConstants
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    : 상수값을 위한 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
public class AccountConstants {
    private AccountConstants() {}

    /** 계좌 상태 기본값 (1 정상) */
    public static final int DEFAULT_ACCOUNT_STATUS = 1;

    /** 계좌 종류 기본값 (1 일반) */
    public static final int DEFAULT_ACCOUNT_TYPE = 1;

    /** 기본 은행코드 */
    public static final String DEFAULT_BANK_CODE = "999";

    /** 기본 지점코드 */
    public static final String DEFAULT_BRANCH_CODE = "001";

    /** 기본 수수료 정책 */
    public static final int DEFAULT_FEE_POLICY_SEQ = 1;

    /** 1일 이체 한도 기본값 */
    public static final long DEFAULT_DAILY_TRANSFER_LIMIT = 3_000_000L;

    /** 1일 출금 한도 기본값 */
    public static final long DEFAULT_DAILY_WITHDRAW_LIMIT = 1_000_000L;

}