package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne
 * Order -> Member
 * Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        return orderRepository.findAllByCriteria(new OrderSearch());
    }

    @GetMapping("/api/v2/simple-orders")
    public Result orderV2() {
        return new Result(orderRepository.findAllByCriteria(new OrderSearch()).stream()
                .map(o -> new SimpleOrderDto(o)).collect(Collectors.toList()));


    }

    @GetMapping("/api/v3/simple-orders")
    public Result orderV3() {
        return new Result(orderRepository.findAllWithMemberDelivery().stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList()));

    }

    @GetMapping("/api/v4/simple-orders")
    public Result orderV4() {
        return new Result(orderSimpleQueryRepository.findOrderDtos());

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


}
