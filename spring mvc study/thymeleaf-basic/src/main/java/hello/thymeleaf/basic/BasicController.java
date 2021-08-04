package hello.thymeleaf.basic;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "Hello Spring!");
        //< , > 사용시 주의
        return "basic/text-basic";
    }

    @GetMapping("text-unescaped")
    public String textUnescaped(Model model) {
        model.addAttribute("data", "Hello <b>Spring!</b>");
        return "basic/text-unescaped";
    }

    @GetMapping("/variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 20);

        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("users", list);
        model.addAttribute("userMap", map);

        return "basic/variable";

    }

    @GetMapping("/test")
    public String testVariable(Model model) {
        Item item1 = new Item(1L, "item1");
        Item item2 = new Item(2L, "item2");


        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Map<String, Item> itemMap = new HashMap<>();
        itemMap.put("item1", item1);
        itemMap.put("item2", item2);

        model.addAttribute("item1", item1);
        model.addAttribute(item2);
        model.addAttribute("items", items);
        model.addAttribute("itemMap", itemMap);

        return "/basic/test";
    }


    @GetMapping("/basic-objects")
    public String basicObjedcts(HttpSession session) {
        session.setAttribute("sessionData", "Hello session");
        return "basic/basic-objects";
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello" + data;
        }
    }

    @GetMapping("/date")
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "/basic/date";
    }

    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";
    }

    @GetMapping("/literal")
    public String literals(Model model) {
        model.addAttribute("data", "Spring!");
        return "basic/literal";
    }

    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "Spring!");
        return "/basic/operation";
    }

    /**
     * th: 속성
     * 속성이 있을 경우 대체, 없으면 만들어줌
     */

    @GetMapping("/attribute")
    public String attribute() {
        return "basic/attribute";
    }

    @GetMapping("/each")
    public String each(Model model) {
        addUsers(model);
        return "/basic/users";

    }

    @GetMapping("/condition")
    public String condition(Model model) {
        addUsers(model);
        return "basic/condition";

    }

    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("data", "Spring!");
        return "basic/comments";
    }

    @GetMapping("/block")
    public String block(Model model) {
        addUsers(model);
        return "/basic/block";
    }

    @GetMapping("/javascript")
    public String inline(Model model) {
        model.addAttribute("user", new User("User\"A\"", 10));
        addUsers(model);
        return "/basic/javascript";
    }

    private void addUsers(Model model) {
        List<User> list = new ArrayList<>();
        list.add(new User("UserA", 10));
        list.add(new User("UserB", 20));
        list.add(new User("UserC", 30));
        model.addAttribute("users", list);
    }


    @Data
    static class User {
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }


}
