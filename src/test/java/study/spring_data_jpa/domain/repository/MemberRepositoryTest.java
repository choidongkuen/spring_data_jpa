package study.spring_data_jpa.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring_data_jpa.domain.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 영속성 컨텍스트의 동일성을 보장
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("jpa test")
    public void testMember() {
        Member member = new Member("최동근", 2);
        System.out.println("==저장 전");
        Member save = this.memberRepository.save(member); // 저장 하기 전 먼저 id 조회 + 저장
        System.out.println("==저장 후==");
        Member findMember = this.memberRepository.findById(save.getId()).get();
        System.out.println("== 조회 후 ==");
        assertThat(findMember).isEqualTo(findMember);
    }

    @Test
    public void findByUsernameAndAgeGraterThanAge() {
        Member m1 = new Member("최동근", 12);
        Member m2 = new Member("박건구", 30);
        Member m3 = new Member("최동근", 20);

        this.memberRepository.save(m1);
        this.memberRepository.save(m2);
        this.memberRepository.save(m3);

        // find 인지 findAll 인지 중요한게 아니라 반환타입에 따라 결정 if 여러개면 List 로 해여함
        List<Member> members = this.memberRepository.findTop1ByUsernameAndAgeGreaterThan("최동근", 2);

        Member member = this.memberRepository.findHelloByUsername("박건구");
        Member memberss = this.memberRepository.findByMembersss("박건구", 2);
        System.out.println("-=");
        Member 박건구 = this.memberRepository.findAllByUsernameAndAge("박건구", 30);

        List<String> result = memberRepository.findAllByUsernameWithNamedQuery();

        List<Member> results = memberRepository.findByNames(Arrays.asList("최동근", "박건구"));
        System.out.println("Query is over");

    }

    @Test
    public void paging() {
        for (int i = 0; i < 20; i++) {
            this.memberRepository.save(new Member(String.valueOf(i + 1), 10));
        }

        System.out.println("20 데이터 저장 완료 !!");

        int age = 10;
        int offset = 1;
        int limit = 3;

        Page<Member> members
                = this.memberRepository.findByAge(age, PageRequest.of(offset, 3, Sort.by(Sort.Direction.DESC, "username")));
        // total count 쿼리 날림

        List<Member> list = members.getContent();
        System.out.println("100 데이터 조회 완료 !!");
        assertThat(members.getSize()).isEqualTo(3);
        assertThat(members.getTotalPages()).isEqualTo(7); // 1 ~ 7
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("최동근1", 12));
        memberRepository.save(new Member("박건구1", 20));
        memberRepository.save(new Member("지승현1", 25));
        memberRepository.save(new Member("권충근2", 30));
        // 영속성 컨텍스트 already not in db

        // 영속성 컨텍스트 무시하고 바로 DB 반영!!
        // @Modifying(clearAutomatically = true) -> 초기화
        int resultCount = memberRepository.bulkAgePlus(10);

        entityManager.flush(); // 영속성 컨텍스트 모두 DB 반영
        entityManager.clear(); // 초기화


        // 영속성 컨텍스트의 데이터를 가져올텐데 이때 [최동근1 의 나이는 그대로 12살]
        Optional<Member> foundMember = memberRepository.findById(1L);
        assertThat(resultCount).isNotEqualTo(-3);
    }
}