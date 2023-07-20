package study.spring_data_jpa.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring_data_jpa.domain.entity.Member;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 영속성 컨텍스트의 동일성을 보장
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

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

}