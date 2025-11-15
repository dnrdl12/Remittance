package io.dnrdl12.remittance.comm.enums;

/**
 * packageName    : io.dnrdl12.remittance.comm.enums
 * fileName       : EntryType
 * author         : JW.CHOI
 * date           : 2025-11-12
 * description    : 거래 원장 분개 유형 (DEBIT / CREDIT)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-12        JW.CHOI            최초 생성
 */
public enum EntryType {
    DEBIT("DEBIT", "차변 (출금 / 자산 감소)"),
    CREDIT("CREDIT", "대변 (입금 / 자산 증가)");

    private final String code;
    private final String description;

    EntryType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static EntryType fromCode(String code) {
        for (EntryType type : EntryType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid EntryType code: " + code);
    }
}