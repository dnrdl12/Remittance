package io.dnrdl12.remittance.repository;

import io.dnrdl12.remittance.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : io.dnrdl12.remittance.repository
 * fileName       : AccountRepository
 * author         : JW.CHOI
 * date           : 2025-11-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-14        JW.CHOI              최초 생성
 */
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account>  {

    /**
     * 동적 검색용 (간단 조회)
     * - Account + Member 까지만 fetch
     */
    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Account> findAll(org.springframework.data.jpa.domain.Specification<Account> spec,
                          Pageable pageable);

    /**
     * 상세 조회용
     * - Account + Member + BalanceSnapshot + FeePolicy fetch
     */
    @EntityGraph(attributePaths = {"member", "balanceSnapshot", "feePolicy"})
    Optional<Account> findByAccountSeq(Long accountSeq);


}