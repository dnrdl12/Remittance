package io.dnrdl12.remittance.comm.enums;

/**
 * packageName    : io.dnrdl12.remittance.comm.enums
 * fileName       : AccountStatus
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
public enum AccountStatus {
    NORMAL(1, "정상"),
    SUSPENDED(2, "정지"),
    CLOSED(3, "해지");

    private final int code;
    private final String description;

    AccountStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // 코드로 enum 가져오기
    public static AccountStatus fromCode(int code) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 상태값입니다: " + code);
    }
}