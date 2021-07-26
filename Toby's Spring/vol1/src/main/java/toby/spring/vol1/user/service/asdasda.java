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

public class asdasda implements UserService{
    UserDao userDao;

    UpgradePolicy upgradePolicy;

    private DataSource dataSource;

    private PlatformTransactionManager transactionManager;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public static final int MIN_LOGCOUNT_FOR_UPGRADE = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUpgradePolicy(UpgradePolicy upgradePolicy) {
        this.upgradePolicy = upgradePolicy;
    }


    @Override
    public void upgradeLevels(){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            upgradeLevelsInternal();
            transactionManager.commit(status);
        } catch (RuntimeException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    public void upgradeLevelsInternal(){
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
