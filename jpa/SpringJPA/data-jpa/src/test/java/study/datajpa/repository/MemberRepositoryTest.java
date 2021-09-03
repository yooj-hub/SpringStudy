package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    void testMember() {
        //given
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(saveMember.getId()).orElse(null);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {

        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member findMember1 = memberRepository.findById(member1.getId()).orElse(null);
        Member findMember2 = memberRepository.findById(member2.getId()).orElse(null);
        List<Member> all = memberRepository.findAll();
        Long count = memberRepository.count();

        //then
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);
        assertThat(all.size()).isEqualTo(2);
        assertThat(all).contains(member1, member2);
        assertThat(count).isEqualTo(2);

        //when
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        Long deletedCount = memberRepository.count();

        //then
        assertThat(deletedCount).isEqualTo(0);


    }

    @Test
    void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        assertThat(result.get(0)).isSameAs(m2);
    }

    @Test
    void findMemberDto() throws Exception {
        //given
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 10, teamA);
        memberRepository.save(m1);

        //when

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        //then
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMembers = memberRepository.findListByUsername("AAA");
        Member findMember = memberRepository.findMemberByUsername("AAA");
        Optional<Member> optionalFindMember = memberRepository.findOptionalMemberByUsername("AAA");
        System.out.println("===============================================================");
        System.out.println("findMembers = " + findMembers);
        System.out.println("findMember = " + findMember);
        System.out.println("optionalFindMember = " + optionalFindMember);
        System.out.println("===============================================================");


    }

    @Test
    void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 1;
        int limit = 3;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> hello = page.map(m -> new MemberDto(m.getId(), m.getUsername(), "hello"));
        System.out.println("========================================================================================");
        System.out.println("hello = " + hello);
        System.out.println("========================================================================================");
        List<Member> topThreeByAgeGreaterThan = memberRepository.findTop3ByAgeGreaterThan(5);
        System.out.println("========================================================================================");
        System.out.println("topThreeByAgeGreaterThan = " + topThreeByAgeGreaterThan);
        System.out.println("========================================================================================");

        //then
        List<Member> content = page.getContent();
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


    }


}
