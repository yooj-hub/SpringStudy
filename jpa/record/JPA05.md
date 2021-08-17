# 고급매핑(상속관계 매핑)

---





- ## 상속관계 매핑

- ## MappedSuperclass





---





## 상속관계 매핑



- 관계형 데이터베이스는 상속 관계 X
- 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
- 상속관계 매핑: 객체의 상속과 구조와 db의 슈퍼타입 서브타입 관계를 매핑



### 전략

- 조인 전략(대표적인 전략)

  - 상위 클래스에 조인하는 식으로 구현 동일한 PK를 가지는 식으로 구현 구분하는 변수가 있어야함

  - 객체를 마다 테이블을 따로 만들고 조인을 이용함

  - 장점

    - 테이블 정규화
    - 외래 키 참조 무결성 제약조건 활용 가능
    - 저장공간 효율화

  - 단점

    - 조회시 조인을 많이 사용, 성능 저하
    - 조회 쿼리가 복잡함
    - 데이터 저장시 Insert가 많이 나감

    

- 통합 테이블로 변환

  - 단일 테이블 전략 한 테이블에 다 넣어 이용하는 전략

  - 쿼리가 나가는 양이 적어 성능이 제일 좋다.

  - 저장

  - 장점

    - 조인이 필요 없으므로 일반적으로 조회 성능이 빠름
    - 조회 쿼리가 단순함

  - 단점

    - 저장 엔티티가 매핑한 컬럼은 모두 null을 허용 해야한다.
    - 단일 테이블이어도, 많은 정보를 저장하면 조회 성능이 조인전략 보다 오히려 느려질 수 잇음

    

- 서브타입 테이브롤 변환

  - 부모 클래스를 제외하고 자식클래스를 각각 다 만드는 전략
  - 조회시 매우 비효율적으로 진행됨
  - 장점
    - 서브 타입을 명확하게 구분해서 처리할 떄 효과적
    - not null 제약 조건 사용가능
  - 단점
    - 여러 자식 테이블을 함께 조회할 때 성능이 느림
    - 자식 테이블을 통합해서 쿼리하기 어려움



기본적으로 JPA는 통합테이블을 이용하는 방식을 지원한다.

```java
@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
@Entity
public class Book extends Item{
    private String author;
    private String isbn;
}
@Entity
public class Movie extends Item{

    private String director;
    private String actor;
}
@Entity
public class Album extends Item{

    private String artist;
}
```





---





## 전략별 Annotation 정리

부모 클래스에 @Inheritance를 이용하여 전략을 이용 가능하다.

```java
package javax.persistence;
//////////////////////////////////////////////////////////////////////
public enum InheritanceType {
    SINGLE_TABLE, // 기본으로 지원하는 싱글테이블 전략
    TABLE_PER_CLASS,// 클래스 1개당 1테이블을 만드는 전략
    JOINED;// 조인 전략

    private InheritanceType() {
    }
}
////////////////////////////////////////////////////////////////////////
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inheritance {
    InheritanceType strategy() default InheritanceType.SINGLE_TABLE;//기본 전략은 싱글
}
```



### 조인 전략



```java
//조인 전략
//부모 클래스 annotation
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn//DTYPE column 데이터 타입을 저장해줌 기본적으로 자식 클래스명(선택 사항 이지만 권장 사항)
//////////////////////////////////////////////////////////////////////////////////
//자식 클래스 annotation
//테이블에 저장된 자식 클래스명을 변경해야 할 경우
@Entity
@DiscriminatorValue("변경하고 싶은 이름")

```





### 단일 테이블 전략



```java
//싱글 테이블 전략
//부모 클래스 annotation
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn//DTYPE column 데이터 타입을 저장해줌 기본적으로 자식 클래스명 (없어도 자동으로 생성)
/////////////////////////////////////////////////////////////////////////////////
//자식 클래스 annotation
//테이블에 저장된 자식 클래스명을 변경해야 할 경우
@Entity
@DiscriminatorValue("변경하고 싶은 이름")
```



###  Table per class



```java
//Table per class 전략 
//부모 클래스 annotation
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ParnentClass{}//추상 클래스로 만들어야한다.
//자식 클래스 annotation
@Entity

```







---

  



## @MappedSuperclass



- 공통된 매핑정보가 필요할 떄 사용
- 매핑 정보만 받는 부모클래스를 생성하여 사용
- 따로 테이블을 생성하지 않음
- 부모타입으로 조회 불가능
- 추상 클래스로 만들어 사용하는게 좋다.



```java
@MappedSuperclass
public class BaseEntity {
  
  //공통된 클래스 정보를 사용
}
// 자식 클래스
@Entity
public class ChildClass extends BaseEntity{}


```





































































