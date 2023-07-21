package study.spring_data_jpa.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.spring_data_jpa.domain.entity.Member;
import study.spring_data_jpa.domain.repository.custom.MemberRepositoryCustom;
import study.spring_data_jpa.dto.MemberDto;

import javax.persistence.QueryHint;
import java.util.List;


// command + e + enter
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    // select m from Member m where m.username = :username and m.age > :age
    List<Member> findTop1ByUsernameAndAgeGreaterThan(String username, int age);

    Member findHelloByUsername(String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    Member findByMembersss(@Param("username") String username, @Param("age") int age);

    Member findAllByUsernameAndAge(String username, int age);

    @Query("select m.username from Member m")
    List<String> findAllByUsernameWithNamedQuery();

    // 컬렉션 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // 페이징
    Page<Member> findByAge(int age, Pageable pageable);

    // 스프링 데이터 JPA 벌크성 수정 쿼리
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >:age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team t")
    List<Member> findMemberFetchJoin();

    // JPQL 없이 페치 조인을 사용 -> 개발자는 굳이 JPQL 을 직접 사용할 필요 x
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    Member findEntityGraphByUsername(String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    Member findByUsernameIsLike(String username);

    @Query("select new study.spring_data_jpa.dto.MemberDto(m.username,m.age) from Member m where m.username = :username")
    List<MemberDto> findMemberDtoByUsername(@Param("username") String username);
}
