package jpabook.jpashop.domain;


import javax.persistence.*;

@Entity

public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

//    @Column(name = "order_id")
//    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


//    @Column(name = "item_id")
//    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    public void setOrder(Order order) {
        this.order = order;
    }
}
