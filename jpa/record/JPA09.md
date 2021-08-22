# JPQL 



## JPA는 다양한 쿼리 방법을 지원





- JPQL
- JPA Criteria
- QueryDSL
- 네이티브 SQL
- JDBC API 직접 사용





---





## JPQL



- JPQL을 사용하는 이유
  - JPA를 사용하면 엔티티 객체를 중심으로 개발
  - 문제는 검색 쿼리
  - 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
  - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
  - 애플리케이션이 필요한 데이터만 db에서 불러오려면 결국 검색조건이 포함된 SQL이 필요
- JPQL
  - JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공
  - JPQL은 에닡티 객체를 대상으로 실행





Ex)

멤버의 username에 kim이 포함된 유저를 찾는 쿼리

```java
List<Member> result = em.createQuery("select m from Member m where m.username like '%kim%'", Member.class).getResultList();
```



---







## JPA Criteria



- 자바 코드로 작성 가능하여 컴파일러를 통해 오류 검증이 가능함
- 너무 복잡하여 실용성이 적다.



```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Member> query = cb.createQuery(Member.class);
Root<Member> m = query.from(Member.class);
CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
List<Member> members = em.createQuery(cq).getResultList();
```





---





## QueryDsl



- 문자가 아닌 자바코드로 JPQL 사용가능
- 단순하고 쉽고, 실무사용 권장





---





## NativeQuery



- em.createNativeQuery("sql",Object.class) 형식으로 이용가능

  





---







## JDBC API

- JPA와 연관 없는 API이용시 flush를해서 db에 강제 저장후 이용해야한다.



---







## JPQL



- 엔티티와 속성은 대소문자 구별
- JPQL 키워드는 대소문자 구분 X
- 엔티티 이름 사용한다.

select m from Member as m where m.age>18



select
	count(m),
	sum(m.age),
	avg(m.age),
	max(m.age),
	min(m.age)
from Member m



---







## Projection



TypeQuery 반환타입이 명확할 경우의 반환타입
Query 반환타입이 명확하지 않을 경우의 변환타입
결과가 하나 이상일 경우 리스트 반환 .gerResultList()
결과가 정확히 하나일 때 getSingleResult();
distinct 를 맨앞에 넣으면 중복이 제거된다.

```java
Member singleResult = em.createQuery("select m from Member as m where m.username = :username", Member.class)
        .setParameter("username", "member1")
        .getSingleResult();
```



2개 이상의 타입을 select 할 경우

```java
//직접 명시
List resultList = em.createQuery("select m.username, m.age from Member as m").getResultList();
Object o = resultList.get(0);
Object[] result = (Object[]) o;
System.out.println("result[0] = " + result[0]);
System.out.println("result[0] = " + result[1]);
```



```java
//제네릭으로 Object[]를 넣어줌
List<Object[]> resultList = em.createQuery("select m.username, m.age from Member as m").getResultList();
for (Object[] objects : resultList) {
    for (Object object : objects) {
        System.out.println("object = " + object);
    }
}
```



```java
//new 사용
List<MemberDto> resultList = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member as m", MemberDto.class).getResultList();
System.out.println("resultList = " + resultList);
```





---







## 페이징





```java
List<Member> resultList = em.createQuery("select m from Member m order by m.age asc ", Member.class).setFirstResult(0).setMaxResults(10).getResultList();
```



.setFirstResult(시작 인덱스)를 넣고 .setMaxResult(가져올 인덱스의 개수)를 넣어서 페이징을 쉽게 할 수 있다.



---







## 조인





내부 조인(해당 변수 내에 대상이 있을 경우)

select m from Member m [inner] join m.team t 



외부 조인(해당 변수 내에 대상이 없을 경우)

select m from Member m left join m.team t



세타 조인(연관관계 없는것을 조인 할 경우)

select count(m) from Member m, Team t where m.username =t.name





On 절

- 조인 대상 필터링
- 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1 부터)



select m, t from Member m left join Team t on m.username = t.name



연관관계 없는 엔티티 외부 조인

select count(m) from Member m, Team t where m.username = t.name





---





## 서브 쿼리 지원 함수





나이가 평균보다 많은 회원

- select m from Member m
  where m.age > (select avg(m2.age)from Member m2)

  

  

한건이라도 주문한 고객

- select m from Member m
  Where (select count(o) from Order o where m = o.member)>0

  

EXISTS 서브쿼리에 결과가 존재하면 참



- ALL ANY SOME
- ALL 모두 만족하면 참
- ANY, SOME 같은 의미, 조건을 하날도 만족하면 참
- 

In 절 하나라도 같은 것이 있으면 참



- Where와 Having 절에서만 서브 쿼리 사용 가능
- select 절도 가능(하이버네이트에서 지원)
- From 절의 서브 쿼리는 현재 JPQL에서 불가능



---





## 조건식



기본 Case 식

select

​	case when m.age <=10 then '학생요금'

​			 when m.age >=10 then '경로요금'

​			 else '일반요금'

​	end

from Member m



단순 Case 식

select

​	case t.name

​				when '팀A' then '인센티브110$'

​				when '팀B' then '인센티브120$'

​				else '인센티브105$'

​	end

from Team t



COALESCE 하나씩 조회해서 null이 아니면 반환 
COALESCE(m.username, 'username is null')을 할 경우 m.username == null일 경우 뒤의 username is null을 반환



NULLIF 두 값이 같으면 null 아닐경우 첫값 반환





## JPQL 기본 함수



- CONCAT
  - 문자열 더하기 지원 CONCAT('a','b')
- SUBSTRING
  - 자바의 substring과 동일
- TRIM
  - 공백 제거
- LOWER, UPPER
  - 대, 소문자
- LENGTH
  - 문자의 길이
- LOCATE
  - 해당 문자열의 위치를 리턴('pattern','pattern')
- ABS,SQRT,MOD
  - 해당 숫자 관련 함수
- SIZE,INDEX(JPA 별도)
  - size는 크기 반환, index는 OrderColumn을 사용할 경우 사용가능





---



> 출처 : 인프런 김영한님의 JPA강의







