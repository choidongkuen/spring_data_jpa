package study.spring_data_jpa.domain.repository;

import org.springframework.stereotype.Repository;
import study.spring_data_jpa.domain.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        // 1. 영속성 컨텍스트의 쓰기 지연 SQL 저장소에 삭제 쿼리 저장
        // 2. Flush 할시 쿼리 적용
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }

    public Optional<Team> findById(long id) {
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }
}

