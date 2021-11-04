package hello.proxy.app.v3;

import hello.proxy.app.v2.OrderServiceV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV3 {
    private final OrderServiceV3 orderService;

    public OrderControllerV3(OrderServiceV3 orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/v3/request")
    public String request(String itemId) {
        this.orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/v3/no-log")
    public String noLog() {
        return null;
    }
}
