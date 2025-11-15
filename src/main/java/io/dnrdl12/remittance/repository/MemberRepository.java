package io.dnrdl12.remittance.repository;

import io.dnrdl12.remittance.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : io.dnrdl12.remittance.repository
 * fileName       : AccountUserRepository
 * author         : JW.CHOI
 * date           : 2025-11-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-12        JW.CHOI              최초 생성
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 필요한 경우 카운트/존재 여부
    boolean existsByMemberCiHash(String memberCiHash);

    Page<Member> findAll(Specification<Member> spec, Pageable pageable);
}