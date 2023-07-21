package study.spring_data_jpa.domain.repository.custom;

import study.spring_data_jpa.domain.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
