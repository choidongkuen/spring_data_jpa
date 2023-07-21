package study.spring_data_jpa.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.spring_data_jpa.domain.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

// 무조건 사용자 정의 레포지토리가 필요한 것은 아니다.
// 그냥 클래스로 임의의 레포지토리를 만들어도 됨
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final EntityManager em;

    List<Member> findMemberList() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
