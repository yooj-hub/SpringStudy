# 즉시 로딩과 지연 로딩



@ManyToOne과 같은 annotation에 fetch 옵션을 이용하여 fetch 를 선택할 수 있다.

```java
/**
- @ManyToOne, @OneToOne은 기본이 즉시 로딩
- @OneToMany, @ManyToMany는 기본이 지연로딩
*/
@ManyToOne(fetch = FetchType.LAZY)//EAGER 선택시 즉시로딩(즉시로딩이 default)
```



lazy의 경우 프록시를 이용하여 가져오기 떄문에 나중에 다시 조회할 경우 또 다른 쿼리를 이용하여 조회를 하게된다.

```java
            Member findMember = em.find(Member.class, member.getId());
            // 지연 로딩이 lazy 일 경우 proxy 를 가져온다.
            System.out.println("findMember.getTeam().getClass() = " + findMember.getTeam().getClass());// proxy

            System.out.println("===========================================");
            // 팀을 통해서 실제 값을 초기화할 떄 쿼리가 나간다.
            System.out.println(findMember.getTeam().getName()); // select 쿼리 실행
            System.out.println("===========================================");
```



Eager 첫 조회때 join을 통해 영속성컨텍스트에 올려놓고 바로바로 출력이 가능하다.

```java
            Member findMember = em.find(Member.class, member.getId());
            // 지연 로딩이 eager일 경우 join을 통해 양쪽 값을 가져온다.
            System.out.println("findMember.getTeam().getClass() = " + findMember.getTeam().getClass());// team class

            System.out.println("===========================================");
						//띠로 쿼리가 나가지 않고 출력이된다.
            System.out.println(findMember.getTeam().getName());
            System.out.println("===========================================");
```



두 값을 자주 같이 쓸 경우 eager 옵션을 쓰는 것이좋다. 





## 프록시와 즉시로딩 주의



- 가급적 지연 로딩만 사용(특히 실무에서)
- 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생
- 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.(첫 쿼리 n 개의 쿼리가 추가로나감)
- @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> Lazy로 설정
- @OneToMany, @ManyToMany는 기본이 지연로딩
- 기본을 lazy를 하고 jpql의 fetch join 및 annotation 등 여러가지 방법을 통해 eager와 같은 옵션을 이용이 가능하다.





---





## 영속성 전이: cascade



- 부모 Entity를 저장할 때 자식 Entity를 저장하고 싶을 때 사용



```java
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)// 부모 엔티티의 매핑된 정보에 사용
```



#### Cascade option

- ALL
- PERSIST
- REMOVE
- MERGE
- REFRESH
- DETACH



### 주의할 점

- 하나의 부모가 자식을 관리할 때 좋다.



### 고아객체

- 부모 엔티티와 연관관계가 끊어진 자식엔티티를 자동으로 삭제한다.
- 옵션을 orphanRemoval = true 사용하면된다.
- 참조하는 곳이 하나일 떄 사용해야한다.
- 특정 엔티티가 개인 소유할 떄 사용 , 공동으로 사용할 경우 예상치 못하게 삭제될 수 있다.
- OneToOne 또는 OneToMany에서 사용가능















