package study.spring_data_jpa.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_data_jpa.domain.entity.Member;

import java.util.List;


// command + e + enter
public interface MemberRepository extends JpaRepository<Member, Long> {

    // select m from Member m where m.username = :username and m.age > :age
    List<Member> findTop1ByUsernameAndAgeGreaterThan(String username, int age);

    Member findHelloByUsername(String username);
}
