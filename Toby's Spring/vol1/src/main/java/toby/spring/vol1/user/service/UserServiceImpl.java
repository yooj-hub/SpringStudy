package toby.spring.vol1.user.service;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import toby.spring.vol1.user.dao.Level;
import toby.spring.vol1.user.dao.UserDao;
import toby.spring.vol1.user.domain.User;
import toby.spring.vol1.user.upgradepolicy.UpgradePolicy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class UserServiceImpl implements UserService{
    UserDao userDao;
    UpgradePolicy upgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUpgradePolicy(UpgradePolicy upgradePolicy) {
        this.upgradePolicy = upgradePolicy;
    }

    @Override
    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for (User user : users) {
            if(upgradePolicy.canUpgradeLevel(user)){
                upgradePolicy.upgradeLevel(user);
            }
        }
    }

//    public void upgradeLevels() {
//        List<User> users = userDao.getAll();
//        for (User user : users) {
//            if (upgradePolicy.canUpgradeLevel((user))) {
//                upgradePolicy.upgradeLevel(user);
//            }
//        }
//    }

    @Override
    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.addUser(user);

    }


}
