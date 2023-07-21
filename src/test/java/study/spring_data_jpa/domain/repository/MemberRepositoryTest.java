package study.spring_data_jpa.domain.repository;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring_data_jpa.domain.entity.Member;
import study.spring_data_jpa.domain.entity.Team;
import study.spring_data_jpa.dto.MemberDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 영속성 컨텍스트의 동일성을 보장
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("jpa test")
    public void testMember() {
        Member member = new Member("최동근", 2);
        System.out.println("==저장 전");
        Member save = this.memberRepository.save(member); // 저장 하기 전 먼저 id 조회 + 저장
        System.out.println("==저장 후==");
        Member findMember = this.memberRepository.findById(save.getId()).get();
        System.out.println("== 조회 후 ==");
        assertThat(findMember).isEqualTo(findMember);
    }

    @Test
    public void findByUsernameAndAgeGraterThanAge() {
        Member m1 = new Member("최동근", 12);
        Member m2 = new Member("박건구", 30);
        Member m3 = new Member("최동근", 20);

        this.memberRepository.save(m1);
        this.memberRepository.save(m2);
        this.memberRepository.save(m3);

        // find 인지 findAll 인지 중요한게 아니라 반환타입에 따라 결정 if 여러개면 List 로 해여함
        List<Member> members = this.memberRepository.findTop1ByUsernameAndAgeGreaterThan("최동근", 2);

        Member member = this.memberRepository.findHelloByUsername("박건구");
        Member memberss = this.memberRepository.findByMembersss("박건구", 2);
        System.out.println("-=");
        Member 박건구 = this.memberRepository.findAllByUsernameAndAge("박건구", 30);

        List<String> result = memberRepository.findAllByUsernameWithNamedQuery();

        List<Member> results = memberRepository.findByNames(Arrays.asList("최동근", "박건구"));
        System.out.println("Query is over");

    }

    @Test
    public void paging() {
        for (int i = 0; i < 20; i++) {
            this.memberRepository.save(new Member(String.valueOf(i + 1), 10));
        }

        System.out.println("20 데이터 저장 완료 !!");

        int age = 10;
        int offset = 1;
        int limit = 3;

        Page<Member> members
                = this.memberRepository.findByAge(age, PageRequest.of(offset, 3, Sort.by(Sort.Direction.DESC, "username")));
        // total count 쿼리 날림

        List<Member> list = members.getContent();
        System.out.println("100 데이터 조회 완료 !!");
        assertThat(members.getSize()).isEqualTo(3);
        assertThat(members.getTotalPages()).isEqualTo(7); // 1 ~ 7
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("최동근1", 12));
        memberRepository.save(new Member("박건구1", 20));
        memberRepository.save(new Member("지승현1", 25));
        memberRepository.save(new Member("권충근2", 30));
        // 영속성 컨텍스트 already not in db

        // 영속성 컨텍스트 무시하고 바로 DB 반영!!
        // @Modifying(clearAutomatically = true) -> 초기화
        int resultCount = memberRepository.bulkAgePlus(10);

        entityManager.flush(); // 영속성 컨텍스트 모두 DB 반영
        entityManager.clear(); // 초기화


        // 영속성 컨텍스트의 데이터를 가져올텐데 이때 [최동근1 의 나이는 그대로 12살]
        Optional<Member> foundMember = memberRepository.findById(1L);
        assertThat(resultCount).isNotEqualTo(-3);
    }

    @Test
    @DisplayName("FETCH JOIN TEST")
    void findMemberByLazy() {
        // given
        Team teamA = new Team("TEAM_A");
        Team teamB = new Team("TEAM_B");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        // when
        this.memberRepository.save(new Member("USER_A", 20, teamA));
        this.memberRepository.save(new Member("USER_A", 20, teamA));
        this.memberRepository.save(new Member("USER_A", 20, teamB));
        this.memberRepository.save(new Member("USER_A", 20, teamB));
        this.memberRepository.save(new Member("USER_A", 20, teamB));

        entityManager.flush();
        entityManager.clear();
        // then

//        List<Member> members = memberRepository.findAll(); // 지연로딩 제외하고 해당 객체만 가져옴
        List<Member> memberList = memberRepository.findMemberFetchJoin(); // inner join 으로 모두 가져옴


        for (Member member : memberList) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.team = " + member.getTeam()); // 지연로딩이기 때문에 이 시점에 실제 TEAM 관련 쿼리가 나간다.(N + 1)
            System.out.println("member.team.class" + member.getTeam().getClass()); // 초기화 되어도 프록시 객체가 실제 객체로 바뀌지는 않는다.
            boolean initialized = Hibernate.isInitialized(member.getTeam());// 지연 로딩 여부를 확인 가능(true)
            System.out.println("======");
            System.out.println(initialized);
        }
        System.out.println("===");
    }

    @Test
    @DisplayName("Entity Grapth TEST")
    void entityGraphTest() {
        // given
        Team teamA = new Team("TEAM_A");
        Team teamB = new Team("TEAM_B");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        // when
        this.memberRepository.save(new Member("USER_A", 20, teamA));
        this.memberRepository.save(new Member("USER_A", 20, teamA));
        this.memberRepository.save(new Member("USER_A", 20, teamB));
        this.memberRepository.save(new Member("USER_A", 20, teamB));
        this.memberRepository.save(new Member("USER_A", 20, teamB));

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.team = " + member.getTeam());
            System.out.println("member.team.class = " + member.getTeam().getClass());
            boolean initialized = Hibernate.isInitialized(member.getTeam());
            System.out.println("====");
        }
        System.out.println("===");
    }

    @Test
    public void queryHint() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        Member member = this.memberRepository.findByUsernameIsLike("%m%");
        System.out.println(member);
    }

    @Test
    public void DtoProjectionTest() {
        // when
        this.memberRepository.save(new Member("USER_A", 20));
        this.memberRepository.save(new Member("USER_A", 20));
        this.memberRepository.save(new Member("USER_A", 20));
        this.memberRepository.save(new Member("USER_A", 20));
        this.memberRepository.save(new Member("USER_A", 20));

        List<MemberDto> userA = this.memberRepository.findMemberDtoByUsername("USER_A");
        System.out.println("===");
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    @DisplayName("Base Entity TEST")
    void jpaEventBaseEntity() throws InterruptedException {

        // given
        Member member = new Member("member1", 20);
        memberRepository.save(member); // @PrePersist

        Thread.sleep(100); // 0.1초
        member.setUsername("member2"); // @PreUpdate

        entityManager.flush();
        entityManager.clear();
        // when
        Member foundMember = memberRepository.findById(member.getId()).get();
        // then

        System.out.println("foundMember.getCreatedAt() = " + foundMember.getCreatedAt());
        System.out.println("foundMember.getUpdatedAt() = " + foundMember.getUpdatedAt());
    }
}