



## 객체와 테이블 매핑



@Entity

- annotation @Entity 가 붙으면 JPA가 관리하고 em을 이용하여 영속성컨텍스트로 이용이 가능하다.
- JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
- 주의
  - 기본 생성자 필수 (public, procted)
  - Final, enum, interface, inner 클래스 사용 x
  - 저장할 필드에 final 사용



@Entity 코드

```java
@Entity // 기본적으로 클래스의 이름을 사용함 따로 사용할 경우 (name = "이름") 으로 사용가능
@Table(name = "테이블명") // 테이블 명을 이용하여 가능
public class Member{
  //...
}
```





---





## 데이터 베이스 스키마 자동생성



- ddl을 애플리케이션 실행 시점에 자동 생성(애플리케이션 실행시 테이블 생성, dialect를 통해 db에 맞는 ddl을 만들어 준다.)
- 테이블 중심 -> 객체 중심
- ddl 옵션에 따라 생성 및 업데이트, 종료시 삭제 등 다양한 옵션이 가능하다.(application.properties or application.yml or persistence.xml 로 설정 가능)
  - Create 시작할 때 해당 테이블이 있으면 drop 후 재 생성
  - Create-drop 시작할 때 해당 테이블이 있으면 drop 후 재 생성 종료시 삭제
  - update 수정분만 반영 단, 삭제는 불가능
  - validate 엔티티와 정상적으로 매핑이 되는지 확인
  - None 옵션 사용 x
  - Create, create-drop, update 의 경우 db 내의 테이블의 수정이 일어날 수 있어 실제 서버 운용시 사용하면 큰 장애를 일으킬 수 있다.



---





## 필드와 컬럼 매핑



### mapping annotataion

@Column

- 옵션
  - name db의 테이블 이름 적용 가능
  - Insertable  등록 가능 여부
  - updatable 변경 가능여부
  - nullable null 가능 여부 (nullable = false 일 경우 not null 제약조건이 생김)
  - Unique unique 제약 조건이 생김 // 단 잘 사용하지 않음 만약 사용해야 할 경우 class 자체에 거는 형식으로 사용
  - Length 길이를 정해줄 수 있음
  - columnDefinition 직접 정보를 삽입 가능 

```java
@Column(name = "asd") // 자바에서는 a 사용 db에서는 column 명이 asd로 설정됨
private String a;
```



@Enumberated

> ORDINAL을 사용할 경우 나중에 유지 보수할 때 정보가 크게 변경되기 쉬워 사용하지 않는것을 권장함

```java
@Enumberated(EnumType.STRING) // enum type 을 사용할 경우 EnumType.STRING 을 사용
private type type 
```



@Temporal

- TemporalType.TIMESTAMP 날짜 시간 다 포함
- TemporalType.DATE 날짜만 포함
- TemporalType.TIME 시간만 포함
- JAVA 8 이후 LocalDate 나 LocalDateTime 을 이용하여 이용하면 더 편하게 가능하다.



@Lob

- 긴 문장을 저장할 때 사용
- H2 database의 경우 clob



@Transient

- 특정 필드를 매핑하지 않을 경우 사용





---





## 기본키 매핑



- @Id 사용 // 직접 값을 할당할 경우 사용
- @GeneratedValue(strategy = GenerationType.AUTO) 데이터 베이스 방언에 맞춰서 생성
- @GeneratedValue(strategy = GenerationType.IDENTITY) db의 기본 전략에 맞춰서 사용 (identity를 사용시 em.persist를 사용시 바로 input을 하게됨)
- @GeneratedValue(strategy = GenerationType.SEQUENCE) Sequence 전략을 사용



#### Table 전략

- 키 생성 전용 테이블을 하나 만들어서 db 시퀀스를 흉내내는 전략이다. db에 종속되지 않고 사용이 가능하나, 성능이 좋지않다.



#### 권장하는 식별자 전략

- 기본키 제약 조건 Null이 아니고 유일해야하며 변하면 안된다.
- 기본적으로 권장하는 전략은 Long형 + 대체키 + 키 생성전략을 사용 



#### Sequence 전략

```java
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR", // seq generator name
        sequenceName = "MEMBER_SEQ", // seq name
        initialValue = 1, allocationSize = 50) // 시작값 1 이후 50씩 증가
```

​	위의 코드와 같이 만들면 처음에 메모리에 50개의 키 값을 가져와서 50개를 배정하고 이후 51이되면 50개를 가져오는 식으로 하여 네트워크 와의 통신 횟수를 낮춰 속도를 높힐 수 있다.



---



### 기본키 매핑

- id 로 이루어진 매핑의 경우 객체지향 보단, 관계형 db형 매핑이다.
- id 로 다시 find를 하여 찾아야 하여 그래프 탐색이 불가능하다.





> 인프런 김영한님의 jpa 강의

