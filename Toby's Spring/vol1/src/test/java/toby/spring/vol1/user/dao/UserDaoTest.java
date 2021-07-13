package toby.spring.vol1.user.dao;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import toby.spring.vol1.user.domain.User;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
//@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoTest {

    //    @Autowired
    private UserDao dao;

    @BeforeEach
    public void setUp() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = ac.getBean("userDao", UserDao.class);
    }


    @Test
    public void 은승원회원가입() throws SQLException, ClassNotFoundException {

        //given
        User user = new User();
        user.setId("1");
        user.setPassword("123");
        user.setName("은승원");

        //when
        dao.addUser(user);

        //then
        assertThat(user.getId()).isEqualTo(dao.get("1").getId());
        dao.deleteByName("은승원");
    }

    @Test
    public void 가입및조회() throws SQLException, ClassNotFoundException {

        //given
        User user1 = new User("esw", "은승원1", "1234");
        User user2 = new User("esw2", "은승원2", "12345");
        User user3 = new User("esw3", "은승원3", "123456");
        dao.deleteAll();

        //when
        assertThat(dao.getCount()).isEqualTo(0);
        dao.addUser(user1);
        dao.addUser(user2);
        dao.addUser(user3);

        //then
        assertThat(dao.getCount()).isEqualTo(3);
        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getId()).isEqualTo(user1.getId());
        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getId()).isEqualTo(user2.getId());
    }

    @Test
    public void 조회실패() throws SQLException {

        //given
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        //when & then
        org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("123123123"));

    }

}
