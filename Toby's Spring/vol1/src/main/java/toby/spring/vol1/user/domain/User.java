package toby.spring.vol1.user.domain;

import lombok.Getter;
import lombok.Setter;
import toby.spring.vol1.user.dao.Level;


@Getter
@Setter
public class User {

    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;
    String email;

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public User(String email) {
        this.email = email;
    }

    public void upgradeLevel(){
        Level nextLevel = this.level.nextLevel();
        if(nextLevel==null){
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }
        else{
            this.level=nextLevel;
        }

    }

    public User() {
    }
}
