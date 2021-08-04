package hello.thymeleaf.basic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Item {
    private Long id;
    private String itemName;

    public Item(Long id, String itemName) {
        this.id = id;
        this.itemName = itemName;
    }
}
