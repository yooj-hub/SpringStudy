

# 스프링 데이터 JPA 페이징과 정렬



```java
@Query(value = "select m from Member m")
Page<Member> findByAge(int age , Pageable pageable);
Page<Member> findByAge(int age , Pageable pageable);
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

    //then
    List<Member> content = page.getContent();
    assertThat(content.size()).isEqualTo(3);
    assertThat(page.getTotalElements()).isEqualTo(5);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.getTotalPages()).isEqualTo(2);
    assertThat(page.isFirst()).isTrue();
    assertThat(page.hasNext()).isTrue();


}
```





```java
Page<Member> page = memberRepository.findByAge(age, pageRequest);
Page<MemberDto> hello = page.map(m -> new MemberDto(m.getId(), m.getUsername(), "hello"));
```