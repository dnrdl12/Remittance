package io.dnrdl12.remittance.comm.enums;

/**
 * packageName    : io.dnrdl12.remittance.comm.enums
 * fileName       : MemberStatus
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
public enum MemberStatus {
    ACTIVE(1, "정상"),   // 정상
    DELETED(2, "삭제");  // 삭제

    private final int code;
    private final String description;

    MemberStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MemberStatus fromCode(int code) {
        for (MemberStatus status : values()) {
            if (status.code == code) return status;
        }
        throw new IllegalArgumentException("지원하지 않는 상태값입니다: " + code);
    }
}