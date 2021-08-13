# 영속성 컨텍스트



## JPA

- 영속성컨텍스트
- 객체와 관계형 데이터베이스 매핑

## 

---





## 영속성 컨텍스트



- 엔티티를 영구저장하는 환경

- 장점
  - 1차 캐시
    - 처음에 데이터 조회시 1차 캐시 테이블 내를 조회하고, 없을 경우 데이터 베이스에서 조회함
  - 동일성 보장
    - 같은 트랜잭션에서 동일성을 보장해준다.
  - 트랜잭션을 지원하는 쓰기 지연
    - SQL 지연 저장소에 저장하여 한번에 SQL을 보낸다.
  - 변경 감지
    - 값을 바꾸고 따로 em.persist()를 하지 않고,  commit을 하면 값이 자동으로 업데이트된다(sql 업데이트 쿼리가 나감)
    - 자바의 collection 과 같이 사용 가능
  - 지연로딩



---





## 엔티티의 생명주기



- 비영속 - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
- 영속 - 영속성 컨텍스트에 관리되는 상태
- 준영속 - 영속성 컨텍스트에 저장되었다가 분리되 ㄴ상태
- 삭제 - 삭제된 상태

### 비영속

- jpa와 전혀 관계가 없는 상태

### 영속

- em.persist()를 이용하여 영속성 컨텍스트에 저장 또는 em.find()를 이용하여 가져온 경우를 말함

```java
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persisteenceUnitName");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {						
            em.persist(객체); // db에 맞는 객체
          	Object findObject = em.find(Object.class, pk); // pk와 해당하는 클래스를 통해 객체를 db또는 1차 캐시를 통해 찾을 수 있음
            tx.commit(); // commit
        } catch (Exception e) {
          tx.rollback();
        } finally {
          em.close();
          emf.close();
        }
    }
}


```



### 준영속

- em.clear(), em.detach(), em.close() 를 할 경우 1차 캐시에서 삭제돼서 변경 감지 또는, 영속상태에서 적용되는 대부분의 기능이 적용되지 않는다.





### 삭제

- em.remove(해당 객체)로 삭제가능



---





## 플러시



- 변경을 감지함
- 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
- 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(등록, 수정, 삭제쿼리)
- 직접 플러시를 하려면 em.flush();(직접 쿼리를 날려서 확인 가능)
- jpql 또는 트랜잭션을 commit 하면 자동으로 호출
- 1차 캐시는 유지하나, 쓰기 지연 sql 저장소에 있는 쿼리를 데이터베이스에 반영



---





> 출처:	인프런 김영한님의 jpa 강의



