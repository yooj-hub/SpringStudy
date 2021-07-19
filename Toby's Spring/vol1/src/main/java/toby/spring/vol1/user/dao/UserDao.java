package toby.spring.vol1.user.dao;

import toby.spring.vol1.user.domain.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();
    void deleteByName(String name);
    public void update(User user);
}
