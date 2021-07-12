package toby.spring.vol1.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    String id;
    String name;
    String password;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User() {
    }
}
