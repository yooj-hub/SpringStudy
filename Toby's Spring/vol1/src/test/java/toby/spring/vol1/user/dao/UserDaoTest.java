package toby.spring.vol1.user.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import toby.spring.vol1.user.domain.User;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static toby.spring.vol1.user.dao.Level.*;

//@SpringBootTest
//@ContextConfiguration(classes = DaoFactory.class)

//@SpringBootTest
public class UserDaoTest {
    private User user1;
    private User user2;
    private User user3;


    @Autowired
    private UserDao dao;

    @BeforeEach
    public void setUp() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = ac.getBean("userDao", UserDaoJdbc.class);
        dao.deleteAll();
        this.user1 = new User("esw", "은승원1", "1234", BASIC, 1, 0);
        this.user2 = new User("esw2", "은승원2", "12345", SILVER, 55, 10);
        this.user3 = new User("esw3", "은승원3", "123456", GOLD, 100, 40);


    }

    @Test
    public void 저장안된_상태에서의_조회() {
        dao.deleteAll();

        List<User> user0 = dao.getAll();
        assertThat(user0.size()).isEqualTo(0);
    }

    @Test
    public void 중복가입확인() {
        dao.addUser(user1);
        Assertions.assertThrows(DataAccessException.class, () -> dao.addUser(user1));

    }


    @Test
    public void 은승원회원가입() throws SQLException, ClassNotFoundException {

        //given
        User user = new User("1", "은승원", "esw123450", GOLD, 1000, 1000);


        //when
        dao.addUser(user);

        //then
        assertThat(user.getId()).isEqualTo(dao.get("1").getId());
        dao.deleteByName("은승원");
    }

    @Test
    public void 가입및조회() throws SQLException, ClassNotFoundException {

        //given


        dao.deleteAll();

        //when
        assertThat(dao.getCount()).isEqualTo(0);
        dao.addUser(user1);
        dao.addUser(user2);
        dao.addUser(user3);


        //then
        assertThat(dao.getCount()).isEqualTo(3);
        assertThat(dao.getAll().size()).isEqualTo(3);
        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getId()).isEqualTo(user1.getId());
        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getId()).isEqualTo(user2.getId());
    }

    @Test
    public void 조회실패() {

        //given
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        //when & then
        org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("123123123"));

    }

    @Test
    public void 업데이트확인() throws Exception {
        //given
        dao.deleteAll();
        dao.addUser(user1);
        dao.addUser(user2);

        //when
        user1.setName("은승이");
        user1.setPassword("123123123");
        user1.setLevel(GOLD);
        user1.setLogin(112312);
        user1.setRecommend(999);
        dao.update(user1);
        dao.update(user2);

        //then
        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2update = dao.get(user2.getId());
        checkSameUser(user2,user2update);

    }


    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());

    }

//    @Test
//    public void getAll(){
//
//    }

}
