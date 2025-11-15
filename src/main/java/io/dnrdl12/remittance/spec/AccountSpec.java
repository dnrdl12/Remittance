package io.dnrdl12.remittance.spec;

import io.dnrdl12.remittance.dto.AccountDto;
import io.dnrdl12.remittance.entity.Account;
import io.dnrdl12.remittance.entity.Member;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Account 엔티티용 동적 검색 Specification
 */
public class AccountSpec {

    /**
     * 가변 검색용 Spec
     *
     * 조건:
     *  - memberNm (like %...%)
     *  - memberSeq (eq)
     *  - accountNumber (eq)
     *  - accountSeq (eq)
     *
     * SearchReq 안에 값이 null/빈값이면 해당 조건은 무시
     */
    public static Specification<Account> search(AccountDto.SearchReq req) {
        return (root, query, cb) -> {
            // 중복 제거
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Member 조인 (이름/회원번호 검색용)
            Join<Account, Member> memberJoin = root.join("member", JoinType.LEFT);

            // 회원명 like 검색
            if (StringUtils.hasText(req.getMemberNm())) {
                predicates.add( cb.like(memberJoin.get("memberNm"), "%" + req.getMemberNm().trim() + "%" ));
            }

            // 회원 번호 =
            if (req.getMemberSeq() != null) {
                predicates.add( cb.equal(memberJoin.get("memberSeq"), req.getMemberSeq()) );
            }

            // 계좌번호 =
            if (StringUtils.hasText(req.getAccountNumber())) {
                predicates.add( cb.equal(root.get("accountNumber"), req.getAccountNumber().trim()) );
            }

            // 계좌 시퀀스 =
            if (req.getAccountSeq() != null) {
                predicates.add( cb.equal(root.get("accountSeq"), req.getAccountSeq()) );
            }

            // 조건이 하나도 없으면 검색
            if (predicates.isEmpty()) {
                throw new RuntimeException("검색 조건이 필요합니다.");
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 상세 조회용 Spec (accountSeq 로 단건 조회)
     *  - 두 번째 쿼리: where a.accountSeq = :accountSeq
     */
    public static Specification<Account> accountSeqEq(Long accountSeq) {
        return (root, query, cb) -> cb.equal(root.get("accountSeq"), accountSeq);
    }
}
