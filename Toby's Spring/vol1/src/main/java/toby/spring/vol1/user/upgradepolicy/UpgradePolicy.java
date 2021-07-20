package toby.spring.vol1.user.upgradepolicy;

import toby.spring.vol1.user.domain.User;

public interface UpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
