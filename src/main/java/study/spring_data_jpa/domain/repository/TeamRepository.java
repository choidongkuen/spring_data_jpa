package study.spring_data_jpa.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_data_jpa.domain.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
