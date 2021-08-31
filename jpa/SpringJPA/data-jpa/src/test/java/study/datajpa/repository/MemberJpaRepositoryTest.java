package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        //given
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(saveMember.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void basicCRUD() {

        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when
        Member findMember1 = memberJpaRepository.find(member1.getId());
        Member findMember2 = memberJpaRepository.find(member2.getId());
        List<Member> all = memberJpaRepository.findAll();
        Long count = memberJpaRepository.count();

        //then
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);
        assertThat(all.size()).isEqualTo(2);
        assertThat(all).contains(member1, member2);
        assertThat(count).isEqualTo(2);

        //when
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        Long deletedCount = memberJpaRepository.count();

        //then
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(new Member("AAA", 10));
        memberJpaRepository.save(m2);
        //when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 15);

        //then
        assertThat(result.get(0)).isSameAs(m2);
    }

    @Test
    void paging() {
        //given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 1;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);


        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);

    }


}
