package io.dnrdl12.remittance.repository;

import io.dnrdl12.remittance.entity.FeePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : io.dnrdl12.remittance.repository
 * fileName       : FeePolicyRepository
 * author         : JW.CHOI
 * date           : 2025-11-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-15        JW.CHOI              최초 생성
 */
public interface FeePolicyRepository extends JpaRepository<FeePolicy, Long> {
}