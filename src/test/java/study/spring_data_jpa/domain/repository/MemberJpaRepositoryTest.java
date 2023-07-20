package study.spring_data_jpa.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring_data_jpa.domain.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void jpaTest() {
        Member member1 = new Member("member1", 23);
        Member member2 = new Member("member2", 27);
        Member member3 = new Member("member3", 50);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        System.out.println("== 영속성 컨텍스트에  저장 ==");

        // 단건 조회
        Member foundMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member foundMember2 = memberJpaRepository.findById(member2.getId()).get();
        Member foundMember3 = memberJpaRepository.findById(member3.getId()).get();

        foundMember1.setUsername("!!!!!");
        System.out.println("== 수정 완료 ==");
        assertThat(foundMember1).isEqualTo(member1);
        assertThat(foundMember2).isEqualTo(member2);


        // 리스트 조회 검증
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("findByUsernameAndAgeGreaterThan")
    void findByUsernameAndAgeGreaterThen() {
        // given
        Member m1 = new Member("최동근", 27);
        Member m2 = new Member("최동근", 22);
        Member m3 = new Member("최동근", 22);
        Member m4 = new Member("최동근", 13);
        Member m5 = new Member("최동근", 6);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);
        // when

        List<Member> allMembers = memberJpaRepository.findAll();
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("최동근", 13);
        // then
        assertThat(result.size()).isLessThan(5);
        assertThat(result.get(0).getUsername()).isEqualTo("최동근");
    }
}