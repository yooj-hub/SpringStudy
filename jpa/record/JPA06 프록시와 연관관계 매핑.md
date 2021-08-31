# 프록시와 연관관계 매핑

---



- 프록시

- 즉시 로딩과 지연 로딩

- 지연 로딩 활용

- 영속성 전이: cascade

- 고아 객체

- 영속성 전이 + 고아 객체, 생명주기

  



---





## 프록시



- em.find() vs em.getReference()

- em.find(): 데이터베이스를 통해서 실제 엔티티 객체 조회

- em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객제 조회

  

### 프록시의 특징

- 실제 클래스를 상속 받아서 만들어짐
- 실제 클래스와 겉 모양이 같다.
- 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨
- 프록시 객체는 처음 사용할 떄 한 번만 초기화
- 프록시 객체를 초기화 할 떄, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초기화 되면 프록시 객체를 통해 실제 엔티티에 접근 가능
- 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함 (==비교 불가, 대신 instanceOf 사용)
- 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
- 영속성 컨텏스트의 도움을 받을 수 없는 준영속 상태일 때 프록시를 초기화 하면 문제 발생





```java
            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.getReference(Member.class, member2.getId());
            Member reference = em.getReference(Member.class, member1.getId());
            /**
             * 양쪽다 find 로 할경우 true
             * 한쪽이 reference 일 경우 false
             * 영속성 컨텍스트에 있을 경우 getReference 도 실제 엔티티를 반환한다.
             * 이미 영속성 컨텍스트에 proxy가 있으면 em.find도 proxy 반환
             */

            System.out.println("m1.getClass() = " + m1.getClass()); // member
            System.out.println("m2.getClass() = " + m2.getClass()); // proxy
            System.out.println("(m1.getClass()==m2.getClass()) = " + (m1.getClass()==m2.getClass())); // false
            System.out.println("reference.getClass() = " + reference.getClass()); // member
            System.out.println("(m1.getClass()==reference.getClass()) = " + (m1.getClass()==reference.getClass()));// true
            System.out.println("m1 instanceof Member : " + (m1 instanceof Member)); // true
            System.out.println("m2 instanceOf Member : " + (m2 instanceof Member)); // true

					////////////////////////////프록시 초기화 방법 및 초기화 확인 방법
						Hibernate.initialize(reference); // 강제 초기화
            boolean loaded = emf.getPersistenceUnitUtil().isLoaded(m2); // 초기화 확인

```





---

