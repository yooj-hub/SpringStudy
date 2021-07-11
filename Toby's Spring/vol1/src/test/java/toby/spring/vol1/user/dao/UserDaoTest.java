package toby.spring.vol1.user.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import toby.spring.vol1.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    @AfterEach
    public void 은승원지우기() throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = ac.getBean("userDao",UserDao.class);
        userDao.deleteByName("은승원");
    }

    @Test
    public void 은승원회원가입() throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = ac.getBean("userDao",UserDao.class);
        User user = new User();
        user.setId("1");
        user.setPassword("123");
        user.setName("은승원");
        userDao.addUser(user);
        Assertions.assertThat(user.getId()).isEqualTo(userDao.get("1").getId());
    }
}
