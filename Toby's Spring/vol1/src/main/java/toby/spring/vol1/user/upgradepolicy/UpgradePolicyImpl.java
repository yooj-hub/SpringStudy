package toby.spring.vol1.user.upgradepolicy;

import org.springframework.beans.factory.annotation.Autowired;
import toby.spring.vol1.user.dao.Level;
import toby.spring.vol1.user.dao.UserDao;
import toby.spring.vol1.user.domain.User;
import toby.spring.vol1.user.service.UserService;

import static toby.spring.vol1.user.service.UserService.MIN_LOGCOUNT_FOR_UPGRADE;
import static toby.spring.vol1.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

public class UpgradePolicyImpl implements UpgradePolicy {
    @Autowired
    UserDao userDao;

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_UPGRADE);
            case SILVER:
                return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " +
                        "currentLevel");
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }






}
