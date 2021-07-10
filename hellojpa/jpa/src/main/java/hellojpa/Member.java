package hellojpa;


import org.hibernate.type.DateType;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {

    public Member() {
    }

    public Member(String username, Integer age, RoleType roleType, LocalDate createDate, LocalDateTime lasModifiedDate, String description, int temp) {
        this.username = username;
        this.age = age;
        this.roleType = roleType;
        this.createDate = createDate;
        this.lasModifiedDate = lasModifiedDate;
        this.description = description;
        this.temp = temp;
    }

    //    @GeneratedValue
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private LocalDate createDate;

    private LocalDateTime lasModifiedDate;


    @Lob// 많은 내용 저장용
    private String description;

    //DB와 연결 안할려면
    @Transient
    private int temp;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLasModifiedDate() {
        return lasModifiedDate;
    }

    public void setLasModifiedDate(LocalDateTime lasModifiedDate) {
        this.lasModifiedDate = lasModifiedDate;
    }
}
