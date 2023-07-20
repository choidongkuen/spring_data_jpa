package study.spring_data_jpa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDto {

    private Long id;
    private String username;
    private int age;

    public MemberDto(Long id, String username, int age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }

    public MemberDto() {
    }
}
