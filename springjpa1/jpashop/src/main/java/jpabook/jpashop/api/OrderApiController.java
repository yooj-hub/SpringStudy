package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public Result ordersV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery();
            order.getOrderItems().stream().forEach(o -> o.getItem().getName());
        }
        return new Result(all);

    }

    @GetMapping("/api/v2/orders")
    public Result ordersV2() {
        return new Result(orderRepository.findAllByCriteria(new OrderSearch())
                .stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList()));

    }

    @GetMapping("/api/v3/orders")
    public Result ordersV3() {
        return new Result(orderRepository.findAllWithItem()
                .stream().map(o -> new OrderDto(o)).collect(Collectors.toList()));
    }

    @GetMapping("/api/v3.1/orders")
    public Result ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit)
    {
        return new Result(orderRepository.findAllWithMemberDelivery(offset,limit)// ToOne 관계를 가져옴
                .stream().map(o -> new OrderDto(o)).collect(Collectors.toList()));
    }

    @Getter
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;


        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems()
                    .stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            orderPrice = orderItem.getOrderPrice();
            itemName = orderItem.getItem().getName();
            count = orderItem.getCount();
        }
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }


}
