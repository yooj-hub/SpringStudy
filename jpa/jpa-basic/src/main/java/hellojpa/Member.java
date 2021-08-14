package hellojpa;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicLong;

@Entity
//@Table(name ="asd") 원하는 테이블에 삽입가능
//@Table(name = "MBR")
//@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50)
public class Member {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO) // db 방언에 맞게 생성
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // db 방언에 맞게 생성
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR") // db 방언에 맞게 생성
    private Long id;
    @Column(name = "name", nullable = false) // username in java // name in db column
    private String username;

    public Member() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
