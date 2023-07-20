package study.spring_data_jpa.domain.repository;

import org.springframework.stereotype.Repository;
import study.spring_data_jpa.domain.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    // 회원(Member) 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 회원(Member) id 조회
    public Member find(long id) {
        return em.find(Member.class, id);
    }

    // 회원(Member) id 조회
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getFirstResult();
    }
}
