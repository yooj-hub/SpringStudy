package toby.spring.vol1.user.service;

import toby.spring.vol1.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
