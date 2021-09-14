# 확장기능

## 사용자 정의 리포지토리 구현



사용자 정의 언터페이스 구현



1. 인터페이스를 만든다.

```java
package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
```

2. 해당 인터페이스의 구현체를 만든다.

```java
package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}

```

3. 기존의 springdatajpa에 extends 한다. (단 기존의 MemberRepositoryImpl 과 같은 이름 형식을 지켜야한다.)

```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {}

```





## Auditing



- 등록일
- 수정일
- 등록자
- 수정자



JPA로 해결



```java
package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
```





SpringDataJpa

1.

```java
package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

}

```

와 같이 스프링 어플리케이션에 annoataion을 단다.



```java
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
```





## 도메인 클래스 컨버터



```java
@GetMapping("/members2/{id}")
public String findMember2(@PathVariable("id") Member member) {
    return member.getUsername();
}
```

다음과 같이 id를 통해 도메인 클래스 컨버터가 작동되어 member가 반환된다. 하지만, 실제로 쓰는것은 조회로만 사용하는 것이 권장된다.



## 페이징

http://localhost:8080/members?page=3&size=3&sort=id,desc

다음과 같이 page에 몇번째 페이지인지(0 부터)

Sort(기준이 무엇인지)

오름차 내림차로 할 수 있다.



Page 기본 설정 바꾸는 법

application.yml

```yml
spring:
  data:
    web:
      pageable:
        default-page-size: 10
        max-page-size: 2000
```



해당 클래스

```java
@GetMapping("/members")
public Page<Member> list(@PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
    return memberRepository.findAll(pageable);
}
```





## 스프링 데이터 JPA 분석



