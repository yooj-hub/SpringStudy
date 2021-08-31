# Spring JPA



h2 data base jdbc:h2:~/datajpa 로 설정해서 만들기





다음과 같이 Spring Data Jpa Repository를 만들 수 있다.

```java
public interface Repository extends JpaRepository<"엔티티", "PK"> {

}
```

주요

save(T)

delete(T)

findById(ID)

getOne(ID)

findAll() sort나 페이징 제공





## 메소드 명으로 쿼리 생성

https://docs.spring.io/spring-data/jpa/docs/2.5.4/reference/html/#reference







## @Query

```java
@Query("select m from Member m where m.username= :username and m.age = :age")
List<Member> findUser(@Param("username") String username, @Param("age") int age);
```

- 오타에 관하여 application 로딩시점에 오류체크를 해준다.



### @Query 를 통해 Dto 조회

```java
@Query("select new study.datajpa.dto.MemberDto(m.id,m.username, t.name) from Member m join m.team t")
List<MemberDto> findMemberDto();
```



@Param("name") Type paramName;
