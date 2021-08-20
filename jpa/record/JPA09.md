# JPQL 



## JPA는 다양한 쿼리 방법을 지원



- JPQL
- JPA Criteria
- QueryDSL
- 네이티브 SQL
- JDBC API 직접 사용





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





## QueryDsl



- 문자가 아닌 자바코드로 JPQL 사용가능
- 단순하고 쉽고, 실무사용 권장



## NativeQuery



- em.createNativeQuery("sql",Object.class) 형식으로 이용가능

  



## JDBC API

- JPA와 연관 없는 API이용시 flush를해서 db에 강제 저장후 이용해야한다.





## JPQL



