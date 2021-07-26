package toby.spring.vol1.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import toby.spring.vol1.user.service.UserService;
import toby.spring.vol1.user.service.UserServiceImpl;
import toby.spring.vol1.user.service.UserServiceTx;
import toby.spring.vol1.user.service.asdasda;
import toby.spring.vol1.user.upgradepolicy.UpgradePolicy;
import toby.spring.vol1.user.upgradepolicy.UpgradePolicyImpl;

@Configuration
public class DaoFactory {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/toby");
        dataSource.setUsername("root");
        dataSource.setPassword("dmstmddnjs32!");

        return dataSource;
    }

    @Bean
    public UserDaoJdbc userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        return transactionManager;
    }

    @Bean
    public JdbcContext jdbcContext() {
        JdbcContext jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource());
        return jdbcContext;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }

    @Bean
    public UserService userService() {
        UserServiceTx userService = new UserServiceTx();
        userService.setUserService(userServiceImpl());
//        userServiceImpl.setUserDao(userDao());
//        userServiceImpl.setUpgradePolicy(upgradePolicy());
//        userServiceImpl.setDataSource(dataSource());
//        userServiceImpl.setTransactionManager(transactionManager());
        return userService;

    }

    @Bean
    public UpgradePolicy upgradePolicy() {
        return new UpgradePolicyImpl();
    }

    @Bean
    public UserServiceImpl userServiceImpl() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setUpgradePolicy(upgradePolicy());
        return userService;
    }
}
