package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
//@Table(name ="asd") 원하는 테이블에 삽입가능
//@Table(name = "MBR")
//@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
//@SequenceGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        sequenceName = "MEMBER_SEQ",
//        initialValue = 1, allocationSize = 50)
public class Member extends BaseEntity{


    //    @GeneratedValue(strategy = GenerationType.AUTO) // db 방언에 맞게 생성
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // db 방언에 맞게 생성
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR") // db 방언에 맞게 생성
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // db 방언에 맞게 생성
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME") // username in java // name in db column
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TEAM_ID")
//    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;

//    @OneToOne(mappedBy = "member")
////    @JoinColumn(name="LOCKER_ID")
//    private Locker locker;

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> products = new ArrayList<>();

    public Member() {
    }


    public List<MemberProduct> getProducts() {
        return products;
    }

    public void setProducts(List<MemberProduct> products) {
        this.products = products;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }


}
