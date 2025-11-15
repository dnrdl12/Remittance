package io.dnrdl12.remittance.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : io.dnrdl12.remittance.projection
 * fileName       : AccountSimpleView
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */

public interface AccountSimpleView {

    Long getAccountSeq();
    String getAccountNumber();
    String getNickname();
    Integer getAccountStatus();
    Integer getAccountType();
    String getBankCode();
    String getBranchCode();

    MemberPart getMember();

    interface MemberPart {
        String getMemberNm();
        String getMemberPhone();
        Integer getMemberStatus();
        String getPrivConsentYn();
        String getMsgConsentYn();
    }
}