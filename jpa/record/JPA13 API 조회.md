# API 개발 고급 -1-



- 조회용 샘플 데이터 입력
- 지연 로딩과 조회 성능 최적화
- 컬렉션 조회 최적화
- 페이징과 한계 돌파
- OSIV와 성능 최적화



## 간단한 주문 조회 Api 작성



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











---



## 컬렉션 조회 최적화



### V1. 엔티티 컬렉션 노출(orderItems)

```java
@GetMapping("/api/v1/orders")
public Result ordersV1() {
    List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
  //프록시 강제 초기화
    for (Order order : all) {
        order.getMember().getName();
        order.getDelivery();
        order.getOrderItems().stream().forEach(o -> o.getItem().getName());
    }
    return new Result(all);

}
```



- 엔티티가 직접 노출되므로 가급적이면 피해야하는 방법이다.
- 



V2. DTO를 사용

```java
@GetMapping("/api/v2/orders")
public Result ordersV2() {
    return new Result(orderRepository.findAllByCriteria(new OrderSearch())
            .stream()
            .map(o -> new OrderDto(o))
            .collect(Collectors.toList()));

}
@Getter
static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItem> orderItems;


    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        orderItems = order.getOrderItems();// null 로 반환
      //아래의 코드로 할경우 JsonIgnore를 설정해두고 Hibernate5를 사용할경우 정상적인 출려이 가능하다.
      //order.getOrderItems().stream().forEach(o -> o.getItem().getName());

    }
}
```

null인 이유는 엔티티를 따로 조회하지 않아 프록시가 반환되기 떄문이다.





다음과 같이 따로 orderItem을 가져올 경우 프록시가아닌 진짜 객체가 반환된다.

```java
    @GetMapping("/api/v3/orders")
    public Result ordersV3() {
        return new Result(orderRepository.findAllWithItem()
                .stream().map(o-> new OrderDto(o)).collect(Collectors.toList()));
    }
///////////////////////////////////////////////////////
public List<Order> findAllWithItem() {
    return em.createQuery(
            // distinct 는 db에서는 효과가 없으나, jpa의 distinct는 중복을 제거해준다.
            "select distinct o from Order o " +
                    "join fetch o.member " +
                    "join fetch o.delivery d " +
                    "join fetch  o.orderItems oi " +
                    "join fetch oi.item",Order.class
    ).getResultList();
}
```





- 다음과 같이 1:N fetch join을 할 경우 페이징이 불가능하다.

- 안되는 이유 다를 기준으로 페이징이 돼서 필요이상의 데이터가 나와 페이징이 되지 않는다
- 중복된 데이터가 많이 조회된다.









## 컬렉션 엔티티를 조회하면서 페이징 하는 법



- 안되는 이유 다를 기준으로 페이징이 돼서 필요이상의 데이터가 나와 페이징이 되지 않는다





1. ToOne 관계를 모두 fetch 조인해도 된다.
2. 컬렉션은 지연로딩으로 조회한다.
3. 지연 로딩 성능 최적화를 위해 BatchSize를 적용한다.



엔티티의 컬렉션에 @BatchSize 사용 및 엔티티 자체에 사용

Application yml에 다음을 적용

jpa.properties.hibernate.default_batch_fetch_size: 100 # 한번에 가져오는 인쿼리의 개수 끝까지 루프를 통해 가져옴//

```yml
jpa.properties.hibernate.default_batch_fetch_size: 100 # 한번에 가져오는 인쿼리의 개수 끝까지 루프를 통해 가져옴//


```



V3.1

```java
public List<Order> findAllWithMemberDelivery(int offset, int limit) {
    return em.createQuery(
                    "select o from Order o " +
                            "join fetch o.member " +
                            "join fetch o.delivery",
                    Order.class
            ).setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
}
```



```java
@GetMapping("/api/v3.1/orders")
public Result ordersV3_page(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "100") int limit)
{
    return new Result(orderRepository.findAllWithMemberDelivery(offset,limit)// ToOne 관계를 가져옴
            .stream().map(o -> new OrderDto(o)).collect(Collectors.toList()));
}
```



다음과 같이 batch사이즈를 설정 할 경우 한번에 여러개를 가져와 1+ N 문제를 batch사이즈에 따라 극복이가능하다.

단 하나의 쿼리는 아니지만 많은 데이터를 가져오게 될 경우 v3에 비해 빠를 수 있다.

페이징이 가능하다.



batch size를 정하는법

- 최대 1000개(DB에 따라 다름)
- 100 ~ 1000 권장(대부분의 경우가 1000개가 좋음 단, 순간적으로 application 과 db에 순간적인 부하가 간다.)





---





JPA에서 DTO 직접 조회

V4

```java
public List<OrderQueryDto> findOrderQueryDtos() {
    List<OrderQueryDto> result = findOrders();

    result.forEach(o -> {
        List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
        o.setOrderItems(orderItems);
    });
    return result;
}

private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, i.stockQuantity) " +
                    " from OrderItem oi" +
                    " join oi.item i" +
                    " where oi.order.id = :orderId", OrderItemQueryDto.class).setParameter("orderId", orderId).getResultList();
}

public List<OrderQueryDto> findOrders() {
    return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate,o.status,d.address) from Order o" +
                    " join o.member m" +
                    " join o.delivery d", OrderQueryDto.class).getResultList();


}
```





다음과 같이 할 경우 OrderItem에서 N+1문제가 발생하게 된다.

V5

```java
public List<OrderQueryDto> findAllByDto_optimization() {
    List<OrderQueryDto> result = findOrders();

    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

    result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
    return result;
}

private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
    List<OrderItemQueryDto> orderItems = em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, i.stockQuantity) " +
                    " from OrderItem oi" +
                    " join oi.item i" +
                    " where oi.order.id in :orderIds", OrderItemQueryDto.class).setParameter("orderIds", orderIds).getResultList();

    Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
    return orderItemMap;
}

private List<Long> toOrderIds(List<OrderQueryDto> result) {
    List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
    return orderIds;
}
```



코드자체가 길지만 기존의 fetch 조인보다 쿼리수가 적다. 하지만, 유지보수가 좋지 않다.

V6

```java
public List<OrderFlatDto> findAllByDto_flat() {
    return em.createQuery(
                    "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status,d.address,i.name,oi.orderPrice,oi.count)" +
                            " from Order o" +
                            " join o.member m" +
                            " join o.delivery d" +
                            " join o.orderItems oi" +
                            " join oi.item i", OrderFlatDto.class)
            .getResultList();


}
```
