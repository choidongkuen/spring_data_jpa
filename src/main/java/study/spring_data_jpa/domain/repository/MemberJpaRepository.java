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

    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    /*
     검색 조건 : 나이가 10살
     정렬 조건 : 이름 내림차순
     페이징 조건 : 첫번째 페이지, 페이지당 보여줄 데이터는 3건
     # offset : 몇번째 페이지
     # limit : 몇개 데이터
     */
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Member> findAllSorting(int age) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
                .setParameter("age", age)
                .getResultList();
    }

    /*
       총 데이터 개수
     */

    public long totalCount(int age) {
        return em.createQuery("select count(m) from  Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
}
