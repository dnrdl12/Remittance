package io.dnrdl12.remittance.comm.config;

import io.dnrdl12.remittance.comm.enums.EntryType;
import io.dnrdl12.remittance.comm.enums.MemberStatus;
import io.dnrdl12.remittance.entity.Member;
import io.dnrdl12.remittance.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * packageName    : io.dnrdl12.remittance.comm.config
 * fileName       : DataInitConfig
 * author         : JW.CHOI
 * date           : 2025-11-14
 * description    : 개발서버 시작 시 초기 데이터 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-14        JW.CHOI              최초 생성
 */
@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataInitConfig  implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) {
        // 회원
        if (memberRepository.count() ==  0) {
            List<Member> dummyMembers = List.of(
                    createMember("최정욱", "01011112222", "CI-001", "DI-001"),
                    createMember("최진후", "01022223333", "CI-002", "DI-002"),
                    createMember("이영희", "01033334444", "CI-003", "DI-003"),
                    createMember("박민수", "01044445555", "CI-004", "DI-004"),
                    createMember("최지우", "01055556666", "CI-005", "DI-005")
            );
            memberRepository.saveAll(dummyMembers);
        }
    }

    /**
     * 회원 생성
     * */
    private Member createMember(String name, String phone, String ci, String di) {
        Member member = new Member();
        member.setMemberNm(name);
        member.setMemberPhone(phone);
        member.setMemberPhoneHash(randomHash());

        member.setMemberCi(ci);
        member.setMemberCiHash(randomHash());

        member.setMemberDi(di);
        member.setMemberDiHash(randomHash());

        member.setMemberStatus(MemberStatus.ACTIVE.getCode());
        member.setPrivConsentYn("Y");
        member.setMsgConsentYn("Y");

        return member;
    }

    // 예시용 랜덤 해시
    private String randomHash() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}