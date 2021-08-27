# API 개발 고급



- 조회용 샘플 데이터 입력
- 지연 로딩과 조회 성능 최적화
- 컬렉션 조회 최적화
- 페이징과 한계 돌파
- OSIV와 성능 최적화



## 간단한 주문 조호 ㅣApi 작성



V1. List<엔티티>로 반환하는 경우



문제 1.

​	양방향 연관관계의 경우 서로 참조하고 있을경우 다음과 같이 할 경우 무한 루프에 빠진다.

```java
@GetMapping("/api/v1/simple-orders")
public List<Order> ordersV1(){
    return orderRepository.findAllByCriteria(new OrderSearch());
}
```

- 해결 방안 -> JsonIgnore로 양방향 영관관계가 있는곳에 Json을 생성하지 않게 한다.



문제 2.

​	만약 fetch 가 Lazy 일 경우 Order 내의 엔티티는 Proxy로 반환된다.

- 해별방안 -> hibernate 5 모듈 사용







V2. List<Dto>로 반환하는 경우(fetch x)



```java
@GetMapping("/api/v2/simple-orders")
public Result orderV2() {
    return new Result(orderRepository.findAllByCriteria(new OrderSearch()).stream()
            .map(o -> new SimpleOrderDto(o)).collect(Collectors.toList()));


}

@Data
@AllArgsConstructor
static class Result<T> {
    private T data;

}

@Data
@AllArgsConstructor
static class SimpleOrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();//LAZY 초기화
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();//LAZY 초기화
    }
}
```



다음과 같이 반환할 경우 Order가 2개가 조회 됐을 때, member와 delivery를 order 조회시마다 db에서 조회하므로 총 쿼리가 5번나가 1+N문제가 발생하게 된다.







V3. List<Dto>로 반환하는 경우(fetch o)



```java
public List<Order> findAllWithMemberDelivery() {
    return em.createQuery(
            "select o from Order o " +
                    "join fetch o.member " +
                    "join fetch o.delivery",
            Order.class
    ).getResultList();
}
//////////////////////////////////////////////////////////
@GetMapping("/api/v3/simple-orders")
    public Result orderV3() {
        return new Result(orderRepository.findAllWithMemberDelivery().stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList()));

    }
```

다음과 같이 할 경우 Order를 조회할 때 fetch join을 통해 Proxy가 아닌 진짜 Entity를 가져와서 쿼리가 1번만 나가고 처리가 된다.





V4. JPA에서 DTO로 바로 조회하는 경우(fetch x)

```java
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id,m.name,o.orderDate,o.status,d.address) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
```

- V3와 V4는 우열을 가리기 힘들다.
- V4가 성능상은 우위이나, 재사용이 힘들다.
- Repository가 화면을 보내는데 사용하는 클래스에 의존하게 된다.(논리적 계층 구조가 깨진다.)
  - package 및 repository 분리를 통해 의존을 없앤다.
- 대부분의 경우 V3와 V4가 성능상의 차이가 거이 없으나, 많은 정보를 한번에 조회해야할 경우 V4로 최적화 하는것을 생각해봐야한다.





선택 요령

1. DTO 사용 및 페치 조인 사용

2. 1이 안되면 DTO 직접 조회
3. 2도 안될 경우 네이티브 sql 이나 JDBC Template 사용

