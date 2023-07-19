package study.spring_data_jpa.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring_data_jpa.domain.entity.Member;

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
        Member member = new Member("최동근");
        System.out.println("==저장 전");
        Member save = this.memberRepository.save(member); // 저장 하기 전 먼저 id 조회 + 저장
        System.out.println("==저장 후==");
        Member findMember = this.memberRepository.findById(save.getId()).get();
        System.out.println("== 조회 후 ==");
        assertThat(findMember).isEqualTo(findMember);
    }
}