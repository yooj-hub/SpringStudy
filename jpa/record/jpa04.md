# 다중성

---



### 종류

- 다대일 @MatyToOne
- 일대다 @OneToMany
- 일대일 @OneToOne
- 다대다 @MatyToMay 실무에서 최대한 피하는게 좋음





### 연관관계의 주인

- 테이블은 외래 키 하나로 두 테이블이 연관관계를 맺음
- 객체 양방향 관계는 A-> B, B-> A 처럼 참조가 2군데
- 객체 양방향 관계는 참조가 2군데 있음. 둘중 테이블의 외래 키를 관리할 곳을 지정해야함
- 연관관계의 주인: 외래 키를 관리하는 참조
- 주인의 반대편: 외래키에 영향을 주지 않음, 단순 조회만 가능





---





## 다대일 (@ManyToOne)



- Many 쪽이 연관관계의 주인
- 가장 많이 사용하는 연관관계
- 일대다의 반대
- 양쪽을 서로 참조하면서 개발할 수 있다.





```java
		//member class 단방향
		@ManyToOne
    private Team team;
		///////////////////////////////////////
		//////////////////////////////////////
		//team class 양방향
		@OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
		

```





## 일대다(@OneToMany)



- 권장하지 않는 모델
- 외래키의 주인이 One
- 일반적으로 다대일 관계에 양방향 관계를 추가하는 식으로 설정하는게 좋다.
- 테이블은 다에 외래키가 있음
- 업데이트시 주인쪽이 아닌 반대편도 업데이트가 되어야한다.



```java
//Team class 단방향
		@OneToMany
    @JoinColumn(name="TEAM_ID")
    private List<Member> members = new ArrayList<>();

//Member class 양방향
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;
```







## 일대일(@OneToOne)



- 일대일 관계는 그 반대도 일대일
- 주 테이블이나 대상 테이블 중에 외래 키 선택 가능
  - 주 테이블에 외래키
  - 대상 테이블에 외래키
- 외래 키에 데이터베이스 유니크 제약조건 추가
- 외래키가 있는 곳이 연관관계의 주인(반대편 mappedBy)
- 주 테이이블에 외래키가 있는 경우
  - JPA 매핑 편리
  - 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
  - 값이 없으면 외래키에 null이 허용됨
- 대상 테이블에 외래키가 있는 경우
  - 대상 테이블에 외래키가 존재
  - 주 테이블과 대상 테이블을 일대일에서 일대다로 변경할 때 구조 유지
  - 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨

```java
	//Member Class 단방향
		@OneToOne
    @JoinColumn(name="LOCKER_ID")
    private Locker locker;

	//Locker Class 양방향
    @OneToOne(mappedBy = "locker")
    private Member member;
```







## 다대다(@ManyToMany)



- 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
- 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야함
- 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 가능
- 중간 테이블을 이용하여 구현이 되나, 중간 테이블을 이용해야해서 한다.(db 에만 테이블 추가)
- 연결 테이블용 엔티티를 추가해서 구현하면 구현 할 수 있다.(새로운 객체 추가 ManyToMany -> ManyToOne & OneToMany)



ManyToMany의 경우 두개로 나누어 구현하는 것이 좋다.

```java
    //Member class
		@OneToMany(mappedBy = "product")
    private List<MemberProduct> products = new ArrayList<>();
		////////////////////////////////////////////////////////////
		//Product class
		@ManyToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();
		///////////////////////////////////////////////////////////
		//MemberProduct class
		@Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID")
    private Product product;


```





---





