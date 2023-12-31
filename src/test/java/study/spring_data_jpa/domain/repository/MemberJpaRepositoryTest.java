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
    @Autowired
    MemberQueryRepository memberQueryRepository;

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

    @Test
    public void paging() {
        for (int i = 0; i < 20; i++) {
            this.memberJpaRepository.save(new Member(String.valueOf(i + 1), 10));
        }

        System.out.println("20 데이터 저장 완료 !!");

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = this.memberJpaRepository.findByPage(age, offset, limit);
        List<Member> sortedMembers = this.memberJpaRepository.findAllSorting(age);
        long totalCount = memberJpaRepository.totalCount(age);
        System.out.println("100 데이터 조회 완료 !!");
    }

    @Test
    @DisplayName("벌크성 수정 쿼리 테스트")
    void bulkUpdate() {
        // given
        memberJpaRepository.save(new Member("member1", 23));
        memberJpaRepository.save(new Member("member2", 50));
        memberJpaRepository.save(new Member("member3", 22));

        // when
        int resultCount = memberJpaRepository.bulkAgePlus(2);
        System.out.println("벌크성 쿼리 완료 나이 + 1");

        // then
        Member member = this.memberJpaRepository.find(1);
        assertThat(resultCount).isEqualTo(3);
    }
}