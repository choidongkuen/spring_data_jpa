package study.spring_data_jpa.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.spring_data_jpa.domain.entity.Member;
import study.spring_data_jpa.dto.MemberDto;

import java.util.List;


// command + e + enter
public interface MemberRepository extends JpaRepository<Member, Long> {

    // select m from Member m where m.username = :username and m.age > :age
    List<Member> findTop1ByUsernameAndAgeGreaterThan(String username, int age);

    Member findHelloByUsername(String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    Member findByMembersss(@Param("username") String username, @Param("age") int age);

    Member findAllByUsernameAndAge(String username, int age);

    @Query("select m.username from Member m")
    List<String> findAllByUsernameWithNamedQuery();

    @Query("select new study.spring_data_jpa.dto.MemberDto(m.id,m.username,m.age) from Member m")
    List<MemberDto> findMemberDto();

    // 컬렉션 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    Page<Member> findByAge(int age, Pageable pageable);
}
