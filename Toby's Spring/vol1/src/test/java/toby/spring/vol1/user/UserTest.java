package toby.spring.vol1.user;

import net.bytebuddy.dynamic.scaffold.FieldLocator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import toby.spring.vol1.user.dao.Level;
import toby.spring.vol1.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void 레벨_업그레이드() {
        //given
        Level[] levels = Level.values();

        //when
        for (Level level : levels) {
            if (level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();

            //then
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test
    public void 레벨_최대치(){
        //given
        Level[] levels = Level.values();
        //when
        for (Level level : levels) {
            if(level.nextLevel()!=null)continue;
            user.setLevel(level);
            //then
            assertThrows(IllegalStateException.class, ()->user.upgradeLevel());
        }
    }
}
