package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Table(name ="asd") 원하는 테이블에 삽입가능
public class Member {

    @Id
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
