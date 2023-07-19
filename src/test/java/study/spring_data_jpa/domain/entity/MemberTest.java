package study.spring_data_jpa.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);


        Member memberA = new Member("memberA", 27, teamA);
        Member memberB = new Member("memberD", 20, teamB);
        Member memberC = new Member("memberC", 20, teamA);
        Member memberD = new Member("memberD", 20, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

        em.flush(); // 영속성 컨텍스트 비우고 + 쿼리 날리기
        em.clear();


        // 확인
        // -> 모든 Member 영속성 컨텍스
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            // N + 1 문제
            System.out.println("member = " + member);
            System.out.println("========");
            System.out.println("-> member.team = " + member.getTeam());
        }
    }
}