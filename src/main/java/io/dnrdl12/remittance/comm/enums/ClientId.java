package io.dnrdl12.remittance.comm.enums;

/**
 * packageName    : io.dnrdl12.remittance.comm.enums
 * fileName       : ClientId
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
public enum ClientId {
    WEB("WEB"),
    APP("APP"),
    BATCH("BATCH"),
    ATM("ATM"),
    BANK("BANK");

    private final String code;

    ClientId(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ClientId fromCode(String code) {
        for (ClientId c : ClientId.values()) {
            if (c.code.equalsIgnoreCase(code)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid ClientId code: " + code);
    }
}