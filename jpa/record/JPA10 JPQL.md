# JPQL



## 경로 표현식

.을 이용하여 객체 그래프 탐색

```
select m.username
	from Member m
	join m.team t
	join m.orders o
 where t.name = '팀A'
```



상태 필드: 단순히 값을 저장하기 위한 필드(단순히 값을 저장하기 위한 필드)

연관필드 연관관계를 위한 필드(연관관계를 위한 필드)

- 단일 값 연관 필드(@ManyToOne, @OneToOne 대상이 엔티티)
- 컬렉션 값 연관 필드(@OneToMany, @ManyToMany 대상이 컬렉션)\







상태필드 : 경로 탐색의 끝, 더이상 탐색 x

단일 값 연관경로: 묵시적 내부 조인 발생(탐색O)

```java
String query = "select m.team from Member m"//묵시적 내부 조인
String query = "select m from Member m join m.team t"
```





컬렉션 값 연관 경로: 묵시적 내부 조인 발생 탐색X (묵시적 내부 조인을 피하는게 좋다.)(t.members.size 는 가능하나 t.members.get()...불가능)

```java
String query = "select t.members from Team t";//size는 사용가능하나 username과 같은 . 그래프 탐색 불가
String query = "select m.username from Team t join t.members m";//다음과 같이 명시적 조인길 경우 그래프 탐색 가능
```



### 최대한 묵시적 조인 대신에 명시적 조인을 사용하는 것이 좋다.

- 향후 쿼리를 수정할 때 묵시적 조인을 할 경우 찾기가 힘들다





---





## 패치 조인



- SQL 조인 종류 X
- JPQL에서 성능 최적화를 위해 제공하는 기능
- 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
- join fetch 명령어 사용



```
[JPQL]
select m from Member m join fetch m.team

```



Fetch join x

```java
//fetch 조인을 사용하지 않은 경우
String query = " select m from Member m";
List<Member> resultList = em.createQuery(query, Member.class).getResultList();
for (Member member : resultList) {
    System.out.println("member = " + member.getUsername()+", member.getTeam().getName() = "+member.getTeam().getName());
}
//조회하지 않은 정보가 있을 때마다 조회용 sql이 나가서 수많은 sql이 나가게 된다.
```



Fetch join O

```java
//fetch 조인을 사용한 경우
String query = " select m from Member m join fetch m.team";
List<Member> resultList = em.createQuery(query, Member.class).getResultList();
for (Member member : resultList) {
    System.out.println("member = " + member.getUsername()+", member.getTeam().getName() = "+member.getTeam().getName());
}
//조회할 때 proxy로 들고오지 않고 한번에 다 가져온다. 따라서, 추가적인 조인이 없다.
```





```java
String query = " select t from Team t join fetch t.members";
List<Team> resultList = em.createQuery(query, Team.class).getResultList();
System.out.println("resultList.size() = " + resultList.size());
for (Team team : resultList) {
    System.out.println("team.getName() = " + team.getName());
    System.out.println("team.getMembers().size() = " + team.getMembers().size());
}
//컬렉션을 가져올 경우 가져온 정보가 필요이상을 가져오게 된다.
            /**
             * resultList.size() = 3
             * team.getName() = TeamA
             * team.getMembers().size() = 2
             * member = Member{id=3, username='member1', age=10}
             * member = Member{id=4, username='member2', age=10}
             * team.getName() = TeamA
             * team.getMembers().size() = 2
             * member = Member{id=3, username='member1', age=10}
             * member = Member{id=4, username='member2', age=10}
             * team.getName() = TeamB
             * team.getMembers().size() = 1
             * member = Member{id=5, username='member3', age=10}
             */
//team의 개수가 3개가 조인됨
```



fetch 조인과 distinct



jpql의 distinct

- SQL에 distinct를 추가
- 애플리케이션에 엔티티 중복 제거





```java
String query = " select distinct t from Team t join fetch t.members";
List<Team> resultList = em.createQuery(query, Team.class).getResultList();
System.out.println("resultList.size() = " + resultList.size());
for (Team team : resultList) {
		System.out.println("team.getName() = " + team.getName());
		System.out.println("team.getMembers().size() = " + team.getMembers().size());
		for (Member member : team.getMembers()) {
			System.out.println("member = " + member);
    }
}

            /**
             * resultList.size() = 2
             * team.getName() = TeamA
             * team.getMembers().size() = 2
             * member = Member{id=3, username='member1', age=10}
             * member = Member{id=4, username='member2', age=10}
             * team.getName() = TeamB
             * team.getMembers().size() = 1
             * member = Member{id=5, username='member3', age=10}
             */
```



#### Fetch 조인의 한계

- 별칭을 줄 수 없다.(하이버네이트는 가능, 가급적 사용하면 안된다.)
- 둘 이상의 컬렉션은 페치 조인 할 수 없다.
- 컬렉션을 페치 조인하면 페이징 API를 사용할 수 없다.
  - 일대일 다대일 같은 단일 값 연관 필드들은 페치 조인을 해도 가능하다.
  - 그 외엔 하이버네이트가 경고로그를 남기낟.







---





## 다형성 쿼리



TYPE

```sql
[JPQL]
select i from Item i
  	where type(i) in (Book, Movie);
  	
[SQL]
select i from i
			where i.DTYPE in ('B','M')
```



TREAT

- 자바의 타입 캐스팅과 유사하고, 상속구조에서 부모타입을 특정 자식타입으로 다룰 때 사용







```sql
TREAT
[JPQL]
select i from Item i
where treat(i as Book).author = 'kim'

[SQL]
select i. * from Item i
where i.DTYPE = 'B' ans i.author = 'kim'


```





---



## JPQL 엔티티 직접 사용



[JPQL]

```sql
select count(m.id) from Member m;
select count(m) from Member m;
```



[SQL]

```sql
select count(m.id) as cnt from Member m;
```



엔티티를 파라미터로 전달이 가능하다.

```java
String jpql = "select m from Member m where m = :member";
List resultList = em.createQuery(jpql)
                    .setParameter("member",member1)
                    .getResultList();
  
```



식별자를 직접 전달

```java
String jpql = "select m from Member m where m.id = :memberId";
List resultList = em.createQuery(jpql)
                    .setParameter("memberId",member1.getId())
                    .getResultList();
  
```





엔티티 직접 사용 - 외래키 값 식별자도 사용 가능



```java
String jpql ="select m from Member m where m.team  = :team ";
List<Member> team = em.createQuery(jpql, Member.class).setParameter("team", teamA).getResultList();
for (Member member : team) {
    System.out.println("member = " + member);
}
```





---





## Named 쿼리 - 어노테이션



- 미리 쿼리를 정의하여 사용이 가능하다. 미리 정의할 경우 컴파일러가 잘못된 문법을 잡아, 오류의 가능성을 줄일 수 있다.

```java
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = : username"
)
//////////////
List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", "member1").getResultList();
```





---



## 벌크 연산

- 한번에 많은 연산을 수행할 경우 사용한다.

```java
//flush 자동 호출
int i = em.createQuery("update Member m set m.age = m.age + 1").executeUpdate();
System.out.println("i = " + i);
System.out.println("member1 = " + member1);//이 때 값이 변하지 않음
Member findMember1 = em.find(Member.class, member1.getId()); // 10 출력
System.out.println("findMember.getAge() = " + findMember1.getAge()); // 10 출럭
//clear를 하여 db에서 값을 가져올 경우 값이 변한다.
em.clear();
Member findMember2 = em.find(Member.class, member1.getId());
System.out.println("findMember2 = " + findMember2);// 11출력
List<Member> select_m_from_member_m = em.createQuery("select m from Member m", Member.class).getResultList();
System.out.println("select_m_from_member_m = " + select_m_from_member_m);
```





- 벌크 연산은 영속성 컨텍스트를 무시하고 테이터베이스에 직접 쿼리
  - 벌크 연산을 먼저 수행
  - 벌크 연산을 수행 후 영속성 컨텍스트 초기화